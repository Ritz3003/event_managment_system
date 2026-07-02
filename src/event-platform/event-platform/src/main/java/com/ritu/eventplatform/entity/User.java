package com.ritu.eventplatform.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private boolean verified;
	
	private LocalDateTime createdAt;
	
	
//	public static void main(String[] args) {
//	    User u = User.builder().build();
//	}

}
