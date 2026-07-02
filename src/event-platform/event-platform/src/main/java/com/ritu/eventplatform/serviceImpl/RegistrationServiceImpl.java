package com.ritu.eventplatform.serviceImpl;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ritu.eventplatform.dto.CancelRegistrationResponse;
import com.ritu.eventplatform.entity.Event;
import com.ritu.eventplatform.entity.Registration;
import com.ritu.eventplatform.entity.Ticket;
import com.ritu.eventplatform.entity.User;
import com.ritu.eventplatform.entity.Waitlist;
import com.ritu.eventplatform.enums.RegistrationStatus;
import com.ritu.eventplatform.enums.TicketStatus;
import com.ritu.eventplatform.exception.RegistrationException;
import com.ritu.eventplatform.repository.EventRepository;
import com.ritu.eventplatform.repository.RegistrationRepository;
import com.ritu.eventplatform.repository.TicketRepository;
import com.ritu.eventplatform.repository.WaitlistRepository;
import com.ritu.eventplatform.security.CurrentUserService;
import com.ritu.eventplatform.service.EmailService;
import com.ritu.eventplatform.service.RegistrationService;

import jakarta.transaction.Transactional;

@Service
public class RegistrationServiceImpl implements RegistrationService {
	
	private final RegistrationRepository registrationRepository;
	private final CurrentUserService currentUserService;

	private final TicketRepository ticketRepository;

	private final EventRepository eventRepository;
	private final WaitlistRepository waitlistRepository;
	private final EmailService emailService;
	
	RegistrationServiceImpl(RegistrationRepository registrationRepository, CurrentUserService currentUserService, 
			TicketRepository ticketRepository, EventRepository eventRepository, WaitlistRepository waitlistRepository,
			EmailService emailService) {
		this.registrationRepository = registrationRepository;
		this.currentUserService = currentUserService;
		this.ticketRepository = ticketRepository;
		this.eventRepository = eventRepository;
		this.waitlistRepository = waitlistRepository;
		this.emailService = emailService;
	}
	
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
	@Transactional
	public CancelRegistrationResponse cancelRegistration(Long registrationId) {
		
		// Find the registration by ID
		
		Registration registration = registrationRepository
		        .findById(registrationId)
		        .orElseThrow(() ->
		                new RegistrationException("Registration not found"));
		
		// check ownership
		 User currentUser = currentUserService.getCurrentUser();
		 
		 if(!registration.getUser().getEmail().equals(currentUser.getEmail())){
			    throw new AccessDeniedException(
			            "You cannot cancel someone else's registration");
			}
		 
		 if (registration.getStatus() == RegistrationStatus.CANCELLED) {
			    throw new RuntimeException(
			            "Registration already cancelled");
			}
		 if (registration.getEvent()
			        .getEventDate()
			        .isBefore(LocalDateTime.now())) {

			    throw new RuntimeException(
			            "Event has already started");
			}
		 Ticket ticket = ticketRepository
			        .findByRegistration(registration)
			        .orElseThrow(() ->
			                new RuntimeException("Ticket not found"));
		 
		 registration.setStatus(RegistrationStatus.CANCELLED);
		 
		 ticket.setStatus(TicketStatus.CANCELLED); 
		 
		 Optional<Waitlist> waitlist =
			        waitlistRepository
			                .findFirstByEventOrderByJoinedAtAsc(
			                        registration.getEvent());
		 
		 if (waitlist.isPresent()) {

			    Waitlist first = waitlist.get();

			  Ticket newTicket =  createRegistration(
			            first.getUser(),
			            registration.getEvent()
			    );
			    
			    emailService.sendWaitlistPromotionEmail(
			            first.getUser().getEmail(),
			            registration.getEvent().getTitle(),
			            newTicket.getTicketNumber()
			    );

			    waitlistRepository.delete(first);
			}
		 
		 
		 registrationRepository.save(registration);

		 ticketRepository.save(ticket);
		 
		 emailService.sendCancellationEmail(
			        currentUser.getEmail(),
			        registration.getEvent().getTitle()
			);
		 
		 return new CancelRegistrationResponse(
			        "Registration cancelled successfully");
	}
	
	

}
