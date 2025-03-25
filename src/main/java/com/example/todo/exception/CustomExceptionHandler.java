package com.example.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    //todolist를 찾지 못했을 때
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<String> handlerTodoNotFound(TodoNotFoundException e, WebRequest req) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    // 저장 실 패 시
    @ExceptionHandler(TodoSaveException.class)
    public ResponseEntity<String> handlerTodoSaveException(TodoSaveException e, WebRequest req) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage()+ "일정 저장 실패");
    }

    //db 관련
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handlerDataAccessException(DataAccessException e, WebRequest req) {
        return new ResponseEntity<>("데이터베이스 오류가 발생했습니다. "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
