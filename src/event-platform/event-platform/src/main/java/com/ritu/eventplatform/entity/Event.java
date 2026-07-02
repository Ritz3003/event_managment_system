package com.ritu.eventplatform.entity;

import java.time.LocalDateTime;

import com.ritu.eventplatform.enums.EventStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(length = 2000)
	private String description;

	private String location;

	//@Column(nullable = false)
	@Column(name = "event_date", nullable = false)
	private LocalDateTime eventDate;

	@Column(nullable = false)
	private Integer capacity;

	//Default is EnumType.ORDINAL, but we want to store the string representation of the enum in the database
	//easy to read and maintain
	//ordinal can lead to issues if the order of the enum constants changes,
	//while string representation is more stable
	@Enumerated(EnumType.STRING)
	private EventStatus status;

	private boolean deleted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User createdBy;
}