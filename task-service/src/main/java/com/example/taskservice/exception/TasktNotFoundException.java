package com.example.taskservice.exception;

public class TasktNotFoundException extends RuntimeException {
    public TasktNotFoundException(String message) {
        super(message);
    }
}