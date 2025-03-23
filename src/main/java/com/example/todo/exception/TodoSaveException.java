package com.example.todo.exception;

public class TodoSaveException extends RuntimeException{
    public  TodoSaveException(String message) {
        super(message + "저장 실패");
    }
}
