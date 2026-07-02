package com.ritu.eventplatform.entity;

import java.time.LocalDateTime;

import com.ritu.eventplatform.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String ticketNumber;
    @Enumerated(EnumType.STRING)
	TicketStatus status;

	LocalDateTime issuedAt;
	@OneToOne
	@JoinColumn(name = "registration_id")
	private Registration registration;
	
	@Column(unique = true, nullable = false)
	private String qrToken;

}
