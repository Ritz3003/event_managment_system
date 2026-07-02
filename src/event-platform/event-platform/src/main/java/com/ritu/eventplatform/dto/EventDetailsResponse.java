package com.ritu.eventplatform.dto;

import java.time.LocalDateTime;

import com.ritu.eventplatform.enums.EventStatus;

public record EventDetailsResponse(

        Long id,
        String title,
        String description,
        String location,
        LocalDateTime eventDate,
        Integer capacity,
        EventStatus status,
        String createdBy

) {}