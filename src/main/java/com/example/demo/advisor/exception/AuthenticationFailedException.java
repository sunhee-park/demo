package com.example.demo.advisor.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message, Throwable cause) {
        super(message);
    }
}