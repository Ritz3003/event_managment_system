package com.ritu.eventplatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.ritu.eventplatform.entity.Event;
import com.ritu.eventplatform.entity.Registration;
import com.ritu.eventplatform.entity.Ticket;
import com.ritu.eventplatform.enums.TicketStatus;

import jakarta.persistence.LockModeType;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	// Add custom query methods if needed
	Optional<Ticket> findByTicketNumber(String ticketNumber);
	
	// Use pessimistic locking to prevent concurrent updates
	// Query to find a ticket by its QR token with a pessimistic write lock
	// Just for better clarity 
	// 
	
	
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT t FROM Ticket t WHERE t.qrToken = :qrToken")
	Optional<Ticket> findWithLockByQrToken(String qrToken);
	
	Optional<Ticket> findByRegistration(Registration registration);
	long countByRegistrationEventAndStatus(
	        Event event,
	        TicketStatus status
	);

}
