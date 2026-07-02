package com.ritu.eventplatform.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ritu.eventplatform.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	// JwtAuthenticationFilter is a custom filter that extends Spring Security's OncePerRequestFilter.
	// It intercepts incoming HTTP requests to extract and validate JWT tokens from the Authorization header.
	// If a valid token is found, it sets the authentication context for the request, allowing access to protected resources.
	// OncePerRequestFilter ensures that the filter is executed only once per request, preventing multiple executions in the filter chain.
	
	private final JwtService jwtService;
	private final CustomUserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	
	// doFilterInternal is the method that contains the logic for filtering incoming requests. 
	//It checks for the presence of a JWT token in the Authorization header, validates it, and sets the authentication context 
	//if the token is valid. If the token is missing or invalid, 
	//it allows the request to proceed without authentication, which may result in access being denied by Spring Security's authorization mechanisms.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
	  // Step 1: Extract the Authorization header from the incoming request
		String header = request.getHeader("Authorization");
		
		//Step 2 : No Header?
		// If the Authorization header is missing or doesn't start with "Bearer ", 
		//we skip the authentication process and allow the request to proceed through the filter chain.
		if(header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		// Step 3: Extract the JWT token from the header
		String token = header.substring(7); // Remove "Bearer " prefix which is of length 7
		
		// step 4: extract the email (username) from the token using the JwtService
		String email = jwtService.extractMail(token);
		
		
		 UserDetails userDetails =
		            userDetailsService.loadUserByUsername(email);

		    if (jwtService.isTokenValid(token)) {

		        UsernamePasswordAuthenticationToken authentication =
		                new UsernamePasswordAuthenticationToken(
		                        userDetails,
		                        null,
		                        userDetails.getAuthorities());

		        authentication.setDetails(
		                new WebAuthenticationDetailsSource()
		                        .buildDetails(request));

		        SecurityContextHolder.getContext()
		                .setAuthentication(authentication);
		    }
		    
		    System.out.println("Token = " + token);
		    System.out.println("Email = " + email);
		    System.out.println("Role = " + userDetails.getAuthorities());

		    filterChain.doFilter(request, response);
		}
		
		
		
	}


