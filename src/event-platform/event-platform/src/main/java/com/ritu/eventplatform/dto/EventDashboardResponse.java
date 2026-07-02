package com.ritu.eventplatform.dto;

public record EventDashboardResponse(

        Long eventId,

        String title,

        Integer capacity,

        Long registered,

        Long cancelled,

        Long checkedIn,

        Long waitlisted,

        Integer availableSeats

) {
}