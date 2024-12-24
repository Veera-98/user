package com.org.user1.exception;

@SuppressWarnings("serial")
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
