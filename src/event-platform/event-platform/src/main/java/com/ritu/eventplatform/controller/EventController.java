package com.ritu.eventplatform.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ritu.eventplatform.dto.CreateEventRequest;
import com.ritu.eventplatform.dto.CreateEventResponse;
import com.ritu.eventplatform.dto.DeleteEventResponse;
import com.ritu.eventplatform.dto.EventDashboardResponse;
import com.ritu.eventplatform.dto.EventDetailsResponse;
import com.ritu.eventplatform.dto.EventSummaryResponse;
import com.ritu.eventplatform.dto.MyEventResponse;
import com.ritu.eventplatform.dto.RegisterEventResponse;
import com.ritu.eventplatform.enums.EventStatus;
import com.ritu.eventplatform.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/events")
public class EventController {
	
	private final EventService eventService;
	
	
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}
	//TODO: Add Swagger annotations for API documentation everywhere in each api calls
	@Operation(summary = "Create a new event")
	@PostMapping
	public CreateEventResponse createEvent(@Valid @RequestBody CreateEventRequest request) {
		return eventService.createEvent(request);
	}
	
	//Older version of getAllEvents without filtering and sorting
//	@GetMapping
//	public Page<EventSummaryResponse> getAllEvents(
//			@RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "2") int size
//			) {
//		return eventService.getAllEvents(page, size);
//	}
	
	@GetMapping
	public Page<EventSummaryResponse> getAllEvents(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) EventStatus status,
	        @RequestParam(required = false) String location,
	        @RequestParam(required = false) String search) {

	    return eventService.getAllEvents(
	            page, size, status, location, search);
	}
	
	@PatchMapping("/{id}/publish")
	public CreateEventResponse publishEvent(@PathVariable Long id) {
	    return eventService.publishEvent(id);
	}
	
	@Operation(
		    summary = "Register for an event",
		    description = "Registers authenticated user for an event"
		)
	@PostMapping("/{id}/register")
	public RegisterEventResponse registerForEvent(
	        @PathVariable Long id) {

	    return eventService.registerForEvent(id);
	}
	
	@GetMapping("/my-events")
	public List<MyEventResponse> getMyEvents() {
	    return eventService.getMyEvents();
	}
	
	@DeleteMapping("/{id}")
	public DeleteEventResponse deleteEvent(
	        @PathVariable Long id) {

	    return eventService.deleteEvent(id);
	}
	
	@GetMapping("/{id}")
	public EventDetailsResponse getEvent(
	        @PathVariable Long id) {

	    return eventService.getEventById(id);
	}
	
	@GetMapping("/{eventId}/dashboard")
	public EventDashboardResponse getDashboard(
	        @PathVariable Long eventId) {

	    return eventService.getDashboard(eventId);
	}

}
