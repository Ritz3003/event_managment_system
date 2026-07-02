package com.ritu.eventplatform.exception;

public class EmailAlreadyExistsException 
 extends RuntimeException {
	public EmailAlreadyExistsException(String message) {
		super(message);
	}

}
