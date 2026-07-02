package com.ritu.eventplatform.service;

import com.ritu.eventplatform.entity.User;

/*
 * JwtService interface defines the contract for generating JSON Web Tokens (JWTs) for user authentication.
 * It provides a method to generate a token based on the provided username.
 * Why Separate Service?
 * We don't want:AuthService to know JWT internals.
 * AuthService
    ↓
  Authentication Logic
   JwtService
    ↓
  Token Logic
*/
public interface JwtService {
	
	String generateToken(User user);		
	String extractMail(String token);
	String extractRole(String token);
	boolean isTokenValid(String token);

}
