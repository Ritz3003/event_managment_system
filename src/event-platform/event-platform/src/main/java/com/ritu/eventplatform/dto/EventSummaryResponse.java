package com.ritu.eventplatform.dto;

import java.time.LocalDateTime;

public record EventSummaryResponse(
		
		Long id, String title, String location, LocalDateTime eventDate, Integer capacity
		
		) {

}
