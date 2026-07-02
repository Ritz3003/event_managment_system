package com.ritu.eventplatform.exception;

public class EventNotFoundException
        extends RuntimeException {

    public EventNotFoundException(String message) {
        super(message);
    }
}