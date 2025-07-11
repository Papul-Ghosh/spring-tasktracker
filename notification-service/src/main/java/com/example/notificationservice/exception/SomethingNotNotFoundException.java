package com.example.notificationservice.exception;

public class SomethingNotNotFoundException extends RuntimeException {
    public SomethingNotNotFoundException(String message) {
        super(message);
    }
}