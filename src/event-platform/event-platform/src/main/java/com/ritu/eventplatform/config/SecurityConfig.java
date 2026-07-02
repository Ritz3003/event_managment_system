package com.ritu.eventplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ritu.eventplatform.security.JwtAuthenticationFilter;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
    	
    	security.csrf(csrf -> csrf.disable())
    	 .sessionManagement(session ->
         session.sessionCreationPolicy(
                 SessionCreationPolicy.STATELESS))
    	  .authorizeHttpRequests(auth -> auth
    			  .requestMatchers(
    					  "/swagger-ui/**",
    				       "/v3/api-docs/**",
                          "/api/auth/**")
                  .permitAll()
                  .anyRequest()
                  .authenticated()
//		          .anyRequest()
//		          .permitAll()
		      )
    	  .addFilterBefore(
                  jwtAuthenticationFilter,
                  UsernamePasswordAuthenticationFilter.class);
    	  
		      //.httpBasic(Customizer.withDefaults()); //not needed as we are using JWT for authentication, 
    	//its for username and password authentication
    	
    	
    	return security.build();
    	
    }
}