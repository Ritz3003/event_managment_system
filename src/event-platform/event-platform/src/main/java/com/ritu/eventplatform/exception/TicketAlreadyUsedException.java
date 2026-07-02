package com.ritu.eventplatform.exception;

public class TicketAlreadyUsedException extends RuntimeException {
	public TicketAlreadyUsedException(String message) {
		super(message);
	}

}
