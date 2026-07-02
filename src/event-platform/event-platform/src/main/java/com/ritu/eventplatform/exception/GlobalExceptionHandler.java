package com.ritu.eventplatform.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ritu.eventplatform.exception.handler.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler({
			EmailAlreadyExistsException.class,
			TicketAlreadyUsedException.class
	}
			)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Map<String,String> handelException(Exception ex){
		
		
		return Map.of("error", ex.getMessage());
	}
	
//	public Map<String,String> handleValidation(
//			MethodArgumentNotValidException ex){
//		
//		String message = ex.getBindingResult()
//				          .getFieldErrors()
//				          .get(0)
//				          .getDefaultMessage();
//		
//		return Map.of("error", message);
//	}
				          
				      

	@ExceptionHandler(InvalidCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Map<String,String> handleInvalidCredentials(InvalidCredentialsException ex){
		
		return Map.of("error", ex.getMessage());
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String,String> handleInvalidPassword(InvalidPasswordException ex){
		
		return Map.of("error", ex.getMessage());
	}
	
	@ExceptionHandler(EventNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEventNotFound(
	        EventNotFoundException ex) {

	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(new ErrorResponse(
	                    ex.getMessage(),
	                    "EVENT_NOT_FOUND",
	                    LocalDateTime.now()
	            ));
	}
	
	@ExceptionHandler({
			UserNotFoundException.class,
			TicketNotFoundException.class
	}
			)
	public ResponseEntity<ErrorResponse> handleUserNotFound(
	        UserNotFoundException ex) {

	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(new ErrorResponse(
	                    ex.getMessage(),
	                    "USER_NOT_FOUND",
	                    LocalDateTime.now()
	            ));
	}
	
	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<ErrorResponse> handleRegistration(
	        RegistrationException ex) {

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ErrorResponse(
	                    ex.getMessage(),
	                    "REGISTRATION_ERROR",
	                    LocalDateTime.now()
	            ));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(
	        Exception ex) {

	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ErrorResponse(
	                    ex.getMessage(),
	                    "INTERNAL_ERROR",
	                    LocalDateTime.now()
	            ));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(
	        MethodArgumentNotValidException ex) {

	    String message = ex.getBindingResult()
	            .getFieldError()
	            .getDefaultMessage();

	    return ResponseEntity.badRequest()
	            .body(new ErrorResponse(
	                    message,
	                    "VALIDATION_ERROR",
	                    LocalDateTime.now()
	            ));
	}
     
}
