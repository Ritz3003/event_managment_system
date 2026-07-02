package com.ritu.eventplatform.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;



public record CreateEventRequest(
         
		@NotBlank(message = "Title is required")
		@Size(max = 100, message = "Title cannot exceed 100 characters")
		String title,
		
		@NotBlank(message = "Description is required")
		String description, 
		
		@NotBlank(message = "Location is required")
		String location, 
		
		@Future(message = "Event date must be in the future")
		LocalDateTime eventDate,
		
		@Positive(message = "Capacity must be a positive number")
		Integer capacity

) {

}
