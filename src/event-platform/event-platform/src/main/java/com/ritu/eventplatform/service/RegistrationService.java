package com.ritu.eventplatform.service;

import com.ritu.eventplatform.dto.CancelRegistrationResponse;

public interface RegistrationService {
	
	CancelRegistrationResponse cancelRegistration(Long registrationId);

}
