package com.ritu.eventplatform.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "waitlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Waitlist {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "event_id", nullable = false)
	    private Event event;

	    @Column(nullable = false)
	    private LocalDateTime joinedAt;

}
