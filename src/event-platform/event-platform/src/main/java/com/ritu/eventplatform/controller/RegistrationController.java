package com.ritu.eventplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ritu.eventplatform.dto.CancelRegistrationResponse;
import com.ritu.eventplatform.service.RegistrationService;


@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    
    RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}
    
    
    @PostMapping("/{registrationId}/cancel")
    public CancelRegistrationResponse cancelRegistration(
            @PathVariable Long registrationId) {
    	
    	return registrationService.cancelRegistration(registrationId);

    }
}