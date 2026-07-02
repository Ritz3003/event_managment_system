package com.ritu.eventplatform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ritu.eventplatform.dto.EventSummaryResponse;
import com.ritu.eventplatform.entity.Event;
import com.ritu.eventplatform.entity.User;
import com.ritu.eventplatform.enums.EventStatus;

import jakarta.persistence.LockModeType;

//I have added JpaSpecificationExecutor to the EventRepository interface to
//support dynamic filtering and sorting of events based on various criteria. 
//This will allow us to implement advanced search functionality in the future if needed.

public interface EventRepository
extends JpaRepository<Event, Long>,JpaSpecificationExecutor<Event> {
	
	List<Event> findByStatusAndDeletedFalse(EventStatus status);
	Optional<Event> findByIdAndDeletedFalse(Long id); // Only normal database calls no locking in this case race condition can occur 
	
	
	List<Event> findByCreatedByAndDeletedFalse(User user);
	Page<Event> findByStatusAndDeletedFalse(EventStatus status, Pageable pageable);
	
	//This method is used to fetch an event by its ID with a pessimistic write lock.
	//Pessimistic locking is used to prevent concurrent updates to the same event by multiple transactions.
	// Real world scenario: When multiple users try to register for the same event at the same time, 
	//this method ensures that only one user can update the event's registration count at a time, preventing overbooking.
	// Here we use Query annotation to define a custom JPQL query that selects the event by its ID and ensures that it is not marked as deleted.
	// but not above because we are using JpaSpecificationExecutor to support dynamic filtering and sorting of events based on various criteria.
	// we can also use @Query annotation without JpaspecificationExecutor to support dynamic filtering and sorting of events based on various criteria.
	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT e FROM Event e WHERE e.id = :id AND e.deleted = false")
	Optional<Event> findByIdForUpdate(@Param("id") Long id);

	
}
