package com.org.user.exception;

@SuppressWarnings("serial")
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
