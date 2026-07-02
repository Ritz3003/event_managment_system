package com.ritu.eventplatform.mapper;

import com.ritu.eventplatform.dto.*;
import com.ritu.eventplatform.entity.Event;

public class EventMapper {

    public static EventSummaryResponse toSummary(Event event) {
        return new EventSummaryResponse(
                event.getId(),
                event.getTitle(),
                event.getLocation(),
                event.getEventDate(),
                event.getCapacity()
        );
    }

    public static MyEventResponse toMyEvent(Event event) {
        return new MyEventResponse(
                event.getId(),
                event.getTitle(),
                event.getStatus()
        );
    }

    public static EventDetailsResponse toDetails(Event event) {
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
}