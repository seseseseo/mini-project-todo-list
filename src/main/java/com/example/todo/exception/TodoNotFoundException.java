package com.example.todo.exception;

public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException(String message) {
        super(message);

    }
    public TodoNotFoundException(Long id){
        super("일정을 찾을 수 없습니다. " + id);
    }
    public TodoNotFoundException(String title, String author){
        super("일정을 찾을 수 없습니다. 제목: " +title + ", 작성자 : " + author);
    }

}
