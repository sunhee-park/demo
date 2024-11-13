package com.example.demo.advisor.exception;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException(String message, Throwable cause ) {
        super(message, cause);
    }
}

