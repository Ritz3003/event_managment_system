package com.ritu.eventplatform.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ritu.eventplatform.dto.ChangePasswordRequest;
import com.ritu.eventplatform.dto.ChangePasswordResponse;
import com.ritu.eventplatform.dto.LoginRequest;
import com.ritu.eventplatform.dto.LoginResponse;
import com.ritu.eventplatform.dto.SignupRequest;
import com.ritu.eventplatform.dto.SignupResponse;
import com.ritu.eventplatform.entity.Role;
import com.ritu.eventplatform.entity.User;
import com.ritu.eventplatform.service.AuthService;
import com.ritu.eventplatform.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;
    private final JwtService jwtService;
	
	public AuthController(AuthService authService , JwtService jwtService) {
		this.authService = authService;
		this.jwtService = jwtService;
	}
	
	
	
	@PostMapping("/signup")
	public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
		
		return authService.signup(request);
	
		
	}
	
	@PostMapping("/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {
		return authService.login(request);
	}
	
	
	@PostMapping("/change-password")
	public ChangePasswordResponse changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		return authService.changePassword(request);
	}
	
	// temporary endpoint to test JWT generation
	
	@GetMapping("/token-test")
	public String tokenTest() {

	    User user = User.builder()
	            .email("ritu@test.com")
	            .role(Role.USER)
	            .build();

	    return jwtService.generateToken(user);
	}
	
	@GetMapping("/validate-token")
	public Map<String, Object> validateToken(
	        @RequestParam String token) {

	    return Map.of(
	            "valid", jwtService.isTokenValid(token),
	            "email", jwtService.extractMail(token),
	            "role", jwtService.extractRole(token)
	    );
	}

}
