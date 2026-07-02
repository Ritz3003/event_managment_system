package com.ritu.eventplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
		
		@NotBlank(message = "Email is required")
		@Email(message = "Invalid email format")
		String email,
		@NotBlank(message = "Old password is required")
		String oldPassword,
		@NotBlank(message = "New password is required")
		@Size(min = 6, message = "New password must be at least 6 characters long")
		String newPassword
		
		) {

}
