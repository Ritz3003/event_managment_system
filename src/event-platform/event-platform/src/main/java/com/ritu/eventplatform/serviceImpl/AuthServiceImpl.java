package com.ritu.eventplatform.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ritu.eventplatform.dto.ChangePasswordRequest;
import com.ritu.eventplatform.dto.ChangePasswordResponse;
import com.ritu.eventplatform.dto.LoginRequest;
import com.ritu.eventplatform.dto.LoginResponse;
import com.ritu.eventplatform.dto.SignupRequest;
import com.ritu.eventplatform.dto.SignupResponse;
import com.ritu.eventplatform.entity.Role;
import com.ritu.eventplatform.entity.User;
import com.ritu.eventplatform.exception.EmailAlreadyExistsException;
import com.ritu.eventplatform.exception.InvalidCredentialsException;
import com.ritu.eventplatform.exception.InvalidPasswordException;
import com.ritu.eventplatform.repository.UserRepository;
import com.ritu.eventplatform.service.AuthService;
import com.ritu.eventplatform.service.JwtService;

import jakarta.transaction.Transactional;




@Service
public class AuthServiceImpl implements AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	
	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}
	
	

	@Override
	public SignupResponse signup(SignupRequest request) {
		
		
		if(userRepository.existsByEmail(request.email())) {
			throw new EmailAlreadyExistsException("Email already exists: " + request.email());
		}
		
		User user = User.builder()
				.name(request.name())
				.email(request.email())
				.password(passwordEncoder.encode( request.password()))
				.role(Role.USER)
				.verified(false)
				.createdAt(LocalDateTime.now())
				.build();
		
		User savedUser = userRepository.save(user);
		
		return new SignupResponse(
				savedUser.getId(), 
				"User registered successfully");
	
		
		//return null;
		
	}



	@Override
	public LoginResponse login(LoginRequest request) {
	 User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
	 
	 boolean matches = passwordEncoder.matches(request.password(), user.getPassword());
	 
	 if(!matches) {
		 throw new InvalidCredentialsException("Invalid email or password");
	 }
	 String token = jwtService.generateToken(user); //  when using jwt instead of  simple login response that return below commented line
		//return new LoginResponse("User logged in successfully");
	 return new LoginResponse(token); //for JWT, we return the token instead of a simple message
	}



	@Override
	@Transactional //write operation, so we need to use @Transactional to ensure that the changes are persisted in the database
	// ASK : If one line fails, do I want all previous DB changes rolled back? Yes, so we use @Transactional 
	public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
		
		User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
		
		boolean oldPasswordMatch = passwordEncoder.matches( request.oldPassword(),user.getPassword());
		
		if(!oldPasswordMatch) {
			throw new InvalidCredentialsException("Invalid email or password");
		}
		if(passwordEncoder.matches(request.newPassword(), user.getPassword())) {
			throw new InvalidPasswordException("New password cannot be the same as the old password");
		}
		
		user.setPassword(passwordEncoder.encode( request.newPassword()));
		
		userRepository.save(user);
		
		
                    
		
		return new ChangePasswordResponse("Password changed successfully");
	}

}
