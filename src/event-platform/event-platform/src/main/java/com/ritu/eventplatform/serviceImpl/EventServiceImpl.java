package com.ritu.eventplatform.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ritu.eventplatform.dto.CreateEventRequest;
import com.ritu.eventplatform.dto.CreateEventResponse;
import com.ritu.eventplatform.dto.DeleteEventResponse;
import com.ritu.eventplatform.dto.EventDashboardResponse;
import com.ritu.eventplatform.dto.EventDetailsResponse;
import com.ritu.eventplatform.dto.EventSummaryResponse;
import com.ritu.eventplatform.dto.MyEventResponse;
import com.ritu.eventplatform.dto.RegisterEventResponse;
import com.ritu.eventplatform.entity.Event;
import com.ritu.eventplatform.entity.Registration;
import com.ritu.eventplatform.entity.Ticket;
import com.ritu.eventplatform.entity.User;
import com.ritu.eventplatform.entity.Waitlist;
import com.ritu.eventplatform.enums.EventStatus;
import com.ritu.eventplatform.enums.RegistrationStatus;
import com.ritu.eventplatform.enums.TicketStatus;
import com.ritu.eventplatform.exception.EventNotFoundException;
import com.ritu.eventplatform.exception.RegistrationException;
import com.ritu.eventplatform.mapper.EventMapper;
import com.ritu.eventplatform.repository.EventRepository;
import com.ritu.eventplatform.repository.RegistrationRepository;
import com.ritu.eventplatform.repository.TicketRepository;
import com.ritu.eventplatform.repository.WaitlistRepository;
import com.ritu.eventplatform.security.CurrentUserService;
import com.ritu.eventplatform.service.EmailService;
import com.ritu.eventplatform.service.EventService;
import com.ritu.eventplatform.specification.EventSpecification;

//import jakarta.transaction.Transactional;

//TODO : Implement DTOMapper

/*
 * User A → locks event row → checks capacity → inserts → unlocks
 * User B → waits → sees updated count → fails safely
 * This is after we have implemented the findByIdForUpdate method in the EventRepository interface and 
 * used it in the registerForEvent method in the EventServiceImpl class. 
 * */


@Service
public class EventServiceImpl implements EventService {
	
	private final EventRepository eventRepository;
	private final TicketRepository ticketRepository;
	private final RegistrationRepository registrationRepository;
	private final CurrentUserService currentUserService;
	private final WaitlistRepository waitlistRepository;
	private final EmailService emailService;
	
	public EventServiceImpl(EventRepository eventRepository,
			RegistrationRepository registrationRepository, CurrentUserService currentUserService,
			TicketRepository ticketRepository, WaitlistRepository waitlistRepository,
			EmailService emailService) {
		this.eventRepository = eventRepository;
//		this.userRepository = userRepository;
		this.registrationRepository = registrationRepository;
		this.currentUserService = currentUserService;
		this.ticketRepository = ticketRepository;
		this.waitlistRepository = waitlistRepository;
		this.emailService = emailService;
	}

	@Override
	public CreateEventResponse createEvent(CreateEventRequest request) {
		
		
		
		
		User user = currentUserService.getCurrentUser();
		
		Event event = new Event();
		event.setTitle(request.title());
		event.setDescription(request.description());
		event.setLocation(request.location());
		event.setEventDate(request.eventDate());
		event.setCapacity(request.capacity());
		event.setStatus(EventStatus.DRAFT);
		event.setDeleted(false);
		event.setCreatedBy(user);
		
		
		Event savedEvent = eventRepository.save(event);
		
		
		
		
		return new CreateEventResponse(
				savedEvent.getId(),
				"Event Created Successfully."
				);
	}

	@Override
	public List<EventSummaryResponse> getAllEvents() {
		List<Event> events = eventRepository.findByStatusAndDeletedFalse(EventStatus.PUBLISHED);
		
		return events.stream().map(event -> new EventSummaryResponse(
				event.getId(),
				event.getTitle(),
				event.getLocation(),
				event.getEventDate(),
				event.getCapacity()
				)).toList();
	
	}

	@Override
	@Transactional
	public CreateEventResponse publishEvent(Long eventId) {
		
		User user = currentUserService.getCurrentUser();
		
		//replacing findByIdAndDeletedFalse to findByIdForUpdate to avoid race condition when multiple users 
		//try to publish the same event at the same time.
//		Event event = eventRepository.findByIdAndDeletedFalse(eventId)
//				.orElseThrow(() -> new EventNotFoundException("Event not found"));
		
		Event event = eventRepository.findByIdForUpdate(eventId)
				.orElseThrow(() -> new EventNotFoundException("Event not found"));
		
		// RULE 1: Only creator can publish
		if(!event.getCreatedBy().getId().equals(user.getId())) {
			throw new RuntimeException("You are not authorized to view this event");
		}
		
		 // RULE 2: Only DRAFT can be published
		if(event.getStatus() != EventStatus.DRAFT) {
			throw new RuntimeException("Only DRAFT events can be published");
		}
		
		event.setStatus(EventStatus.PUBLISHED);
		eventRepository.save(event);
		
				
		
		return new CreateEventResponse(
				event.getId(),
				"Event Published Successfully."
				);
	}
	
	//for WaitList
	private Ticket createRegistration(User user, Event event) {

	    Registration registration = new Registration();

	    registration.setUser(user);
	    registration.setEvent(event);
	    registration.setRegisteredAt(LocalDateTime.now());
	    registration.setStatus(RegistrationStatus.REGISTERED);

	    registrationRepository.save(registration);

	    Ticket ticket = new Ticket();

	    ticket.setRegistration(registration);

	    ticket.setIssuedAt(LocalDateTime.now());

	    ticket.setStatus(TicketStatus.ACTIVE);

	    String ticketNumber =
	            "EVT-" + UUID.randomUUID()
	                    .toString()
	                    .substring(0, 8)
	                    .toUpperCase();

	    ticket.setTicketNumber(ticketNumber);

	    ticket.setQrToken(UUID.randomUUID().toString());

	    ticketRepository.save(ticket);

	    return ticket;
	}
	
	@Override
	@Transactional(
		    isolation = Isolation.READ_COMMITTED
		)
	public RegisterEventResponse registerForEvent(Long eventId) {

		// Step A — Get Logged In User
		User user = currentUserService.getCurrentUser();

		// Step B — Get Event
		// avoid race condition when multiple users 
//		Event event = eventRepository.findByIdAndDeletedFalse(eventId)
//				.orElseThrow(() -> new EventNotFoundException("Event not found"));
		Event event = eventRepository.findByIdForUpdate(eventId)
				.orElseThrow(() -> new EventNotFoundException("Event not found"));

		// Step C — Check Published

		if (event.getStatus() != EventStatus.PUBLISHED) {
			throw new RegistrationException("Event is not published yet");
		}

		// Step D — Prevent Duplicate Registration
		boolean alreadyRegistered = registrationRepository.existsByUserAndEvent(user, event);

		if (alreadyRegistered) {
			throw new RegistrationException("Already registered");
		}

//		Step E — Capacity Check
		
		long currentRegistrations =
		        registrationRepository.countByEventAndStatus(
		                event,
		                RegistrationStatus.REGISTERED
		        );

//		long currentRegistrations = registrationRepository.countByEvent(event);
//
		//Before Waitlist implementation
//		if (currentRegistrations >= event.getCapacity()) {
//			throw new RegistrationException("Event is full");
//		}
		
		if (currentRegistrations >= event.getCapacity()) {

		    boolean alreadyWaiting =
		            waitlistRepository.existsByUserAndEvent(user, event);

		    if (alreadyWaiting) {
		        throw new RegistrationException(
		                "You are already in the waitlist");
		    }

		    Waitlist waitlist = new Waitlist();

		    waitlist.setUser(user);
		    waitlist.setEvent(event);
		    waitlist.setJoinedAt(LocalDateTime.now());

		    waitlistRepository.save(waitlist);

		    return new RegisterEventResponse(
		            "Event is full. You have been added to the waitlist.",
		            null
		    );
		}

//		// Step F — Save Registration
//		Registration registration = new Registration();
//
//		registration.setUser(user);
//		registration.setEvent(event);
//		registration.setRegisteredAt(LocalDateTime.now());
//		registration.setStatus(RegistrationStatus.REGISTERED);
//
//		registrationRepository.save(registration);
//		
//		// this is added after ticket implementation is done. Ticket is generated when user registers for an event.
//		// before that we are simply returing registed successfully message. Now we are generating ticket and returning ticket id in the response.
//		Ticket ticket = new Ticket();
//		ticket.setRegistration(registration);
//
//		ticket.setIssuedAt(LocalDateTime.now());
//
//		ticket.setStatus(TicketStatus.ACTIVE);
//		
//		// for now we are generating ticket number as random string. In future we can implement a better ticket number generation logic.
//		String ticketNumber =
//		        "EVT-" + UUID.randomUUID()
//		                     .toString()
//		                     .substring(0,8)
//		                     .toUpperCase();
//
//		ticket.setTicketNumber(ticketNumber);
//		// New Addition: Generate a unique QR token for the ticket
//		ticket.setQrToken(
//		        UUID.randomUUID().toString()
//		);
//		ticketRepository.save(ticket);
//		Step G — Return Response
		
		

		
		// Before ticket implementation we were returning only event id and registered successfully message. Now we are returning ticket number in the response.
		//return new RegisterEventResponse(event.getId(), "Registered Successfully.");
//		return  new RegisterEventResponse(
//
//		        "Registration successful",
//
//		        ticketNumber
//		);

		
		Ticket ticket = createRegistration(user, event);
		
		//Small Test
		// Send confirmation email
		emailService.sendRegistrationEmail(
		        user.getEmail(),
		        event.getTitle(),
		        ticket.getTicketNumber()
		);

		return new RegisterEventResponse(
		        "Registration successful",
		        ticket.getTicketNumber()
		);
	}

	@Override
	public List<MyEventResponse> getMyEvents() {
		
		User user = currentUserService.getCurrentUser();
		
		List<Event> events = eventRepository.findByCreatedByAndDeletedFalse(user);
		
		
		//Here the return statement internally works like this:
		//List<MyEventResponse> responseList = new ArrayList<>();
		//for(Event event : events) {
		//	responseList.add(new MyEventResponse(event.getId(), event.getTitle(), event.getStatus()));
		//}

	    return events.stream()
	            .map(event -> new MyEventResponse(
	                    event.getId(),
	                    event.getTitle(),
	                    event.getStatus()
	            ))
	            .toList();
	}
	
	@Override
	@Transactional
	public DeleteEventResponse deleteEvent(Long eventId) {

		User user = currentUserService.getCurrentUser();

//	    Event event = eventRepository
//	            .findByIdAndDeletedFalse(eventId)
//	            .orElseThrow(() ->
//	                    new EventNotFoundException("Event not found"));
		
		Event event = eventRepository.findByIdForUpdate(eventId)
	            .orElseThrow(() ->
	                    new EventNotFoundException("Event not found"));

	    if (!event.getCreatedBy()
	            .getId()
	            .equals(user.getId())) {

	        throw new RuntimeException(
	                "You can only delete your own events");
	    }

	    event.setDeleted(true);

	    eventRepository.save(event);

	    return new DeleteEventResponse(
	            "Event deleted successfully");
	}

		@Override
		@Transactional
		public EventDetailsResponse getEventById(Long eventId) {

//		    Event event = eventRepository
//		            .findByIdAndDeletedFalse(eventId)
//		            .orElseThrow(() ->
//		                    new EventNotFoundException("Event not found"));
			
			Event event = eventRepository.findByIdForUpdate(eventId)
		            .orElseThrow(() ->
		                    new EventNotFoundException("Event not found"));

		    return new EventDetailsResponse(
		            event.getId(),
		            event.getTitle(),
		            event.getDescription(),
		            event.getLocation(),
		            event.getEventDate(),
		            event.getCapacity(),
		            event.getStatus(),
		            event.getCreatedBy().getEmail()
		    );
		
		
	}

	@Override
	public Page<EventSummaryResponse> getAllEvents(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Event> events = eventRepository.findByStatusAndDeletedFalse(EventStatus.PUBLISHED, pageable);

		return events.map(EventMapper::toSummary);
	}
	//TODO Understand the Specification and how it works in Spring Data JPA. Implement the getAllEvents method with filtering and searching capabilities using Specification.
	@Override
	public Page<EventSummaryResponse> getAllEvents(
	        int page,
	        int size,
	        EventStatus status,
	        String location,
	        String search) {

	    Pageable pageable = PageRequest.of(page, size);

	    Specification<Event> spec =
	            Specification.where(EventSpecification.isNotDeleted())
	                    .and(EventSpecification.hasStatus(status))
	                    .and(EventSpecification.hasLocation(location))
	                    .and(EventSpecification.searchTitle(search));

	    Page<Event> events = eventRepository.findAll(spec, pageable);

	    return events.map(EventMapper::toSummary);
	}

	@Override
	public EventDashboardResponse getDashboard(Long eventId) {
		
		User user = currentUserService.getCurrentUser();
		
		Event event = eventRepository
		        .findByIdAndDeletedFalse(eventId)
		        .orElseThrow(() ->
		                new EventNotFoundException("Event not found"));
		
		if (!event.getCreatedBy().getId().equals(user.getId())) {
		    throw new AccessDeniedException(
		            "You are not allowed to view this dashboard");
		}
		
		long registered =
		        registrationRepository.countByEventAndStatus(
		                event,
		                RegistrationStatus.REGISTERED
		        );
		
		long cancelled =
		        registrationRepository.countByEventAndStatus(
		                event,
		                RegistrationStatus.CANCELLED
		        );
		
		long checkedIn =
		        ticketRepository.countByRegistrationEventAndStatus(
		                event,
		                TicketStatus.USED
		        );
		
		long waitlisted =
		        waitlistRepository.countByEvent(event);
		
		int availableSeats =
		        event.getCapacity() - (int) registered;

		return new EventDashboardResponse(

		        event.getId(),

		        event.getTitle(),

		        event.getCapacity(),

		        registered,

		        cancelled,

		        checkedIn,

		        waitlisted,

		        availableSeats
		);
	}
	
	
	

}
