package com.ritu.eventplatform.exception;

public class UserNotFoundException
        extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}