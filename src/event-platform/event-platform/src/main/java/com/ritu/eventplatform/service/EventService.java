package com.ritu.eventplatform.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ritu.eventplatform.dto.CreateEventRequest;
import com.ritu.eventplatform.dto.CreateEventResponse;
import com.ritu.eventplatform.dto.DeleteEventResponse;
import com.ritu.eventplatform.dto.EventDashboardResponse;
import com.ritu.eventplatform.dto.EventDetailsResponse;
import com.ritu.eventplatform.dto.EventSummaryResponse;
import com.ritu.eventplatform.dto.MyEventResponse;
import com.ritu.eventplatform.dto.RegisterEventResponse;
import com.ritu.eventplatform.enums.EventStatus;


public interface EventService {
	
	CreateEventResponse createEvent(CreateEventRequest request);
	List<EventSummaryResponse> getAllEvents();
	CreateEventResponse publishEvent(Long eventId);
    RegisterEventResponse registerForEvent( Long eventId);
    List<MyEventResponse> getMyEvents();
    DeleteEventResponse deleteEvent(Long eventId);
    EventDetailsResponse getEventById(Long eventId);
    Page<EventSummaryResponse> getAllEvents(int page, int size);
    Page<EventSummaryResponse> getAllEvents(int page, int size, EventStatus status, String location, String search);
    EventDashboardResponse getDashboard(Long eventId);

}
