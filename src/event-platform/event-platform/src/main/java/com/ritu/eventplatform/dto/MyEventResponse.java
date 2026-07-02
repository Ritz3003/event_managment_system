package com.ritu.eventplatform.dto;

import com.ritu.eventplatform.enums.EventStatus;

public record MyEventResponse(
		
		Long id, String title, EventStatus status
		
		) {

}
