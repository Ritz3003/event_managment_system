package com.ritu.eventplatform.entity;

import java.time.LocalDateTime;

import com.ritu.eventplatform.enums.RegistrationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registration {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// ManyToOne relationship with User entity
	// Many registrations can be associated with one user
	// The @JoinColumn annotation specifies the foreign key column in the registrations table that references the primary key of the users table
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	// ManyToOne relationship with Event entity
	// Many registrations can be associated with one event
	// The @JoinColumn annotation specifies the foreign key column in the registrations table that references the primary key of the events table
	@ManyToOne
	@JoinColumn(name = "event_id")
	private Event event;

	private LocalDateTime registeredAt;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RegistrationStatus status = RegistrationStatus.REGISTERED;

}
