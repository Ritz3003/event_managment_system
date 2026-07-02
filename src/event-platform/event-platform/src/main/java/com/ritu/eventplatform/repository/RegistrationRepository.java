package com.ritu.eventplatform.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ritu.eventplatform.entity.Event;
import com.ritu.eventplatform.entity.Registration;
import com.ritu.eventplatform.entity.User;
import com.ritu.eventplatform.enums.RegistrationStatus;
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
	
	Optional<Registration> findById(Long id);
	
	boolean existsByUserAndEvent(
	        User user,
	        Event event);

	long countByEvent(Event event);
	
	//CancelRegistrationResponse cancelRegistration(Long registrationId);
	
	long countByEventAndStatus(Event event,
            RegistrationStatus status);
	
	//long countByEventAndStatus(Event event, RegistrationStatus status);

}
