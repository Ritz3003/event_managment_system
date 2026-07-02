package com.ritu.eventplatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ritu.eventplatform.entity.Event;
import com.ritu.eventplatform.entity.User;
import com.ritu.eventplatform.entity.Waitlist;

public interface WaitlistRepository
extends JpaRepository<Waitlist, Long> {
	
	boolean existsByUserAndEvent(User user, Event event);
	
	//FIFO - first in first out, so we need to find the first user who joined the waitlist for a specific event
	// THIS is smilary to db query select * from waitlist where event_id = ? order by joined_at asc limit 1;
	Optional<Waitlist> findFirstByEventOrderByJoinedAtAsc(Event event);
	long countByEvent(Event event);

}