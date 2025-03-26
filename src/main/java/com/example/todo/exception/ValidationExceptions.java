package com.example.todo.exception;

public class ValidationExceptions extends RuntimeException {
    public ValidationExceptions(String message) {
        super(message);
    }
}
