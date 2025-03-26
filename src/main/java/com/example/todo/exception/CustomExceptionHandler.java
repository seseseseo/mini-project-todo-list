package com.example.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
                .body( "일정 데이터를 저장하는 도중 오류가 발생했습니다. " +e.getMessage());
    }

    //db 연결 실패
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handlerDataAccessException(DataAccessException e, WebRequest req) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("데이터베이스 연결에 실패했습니다. " + e.getMessage());
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<String> handlerPasswordException(PasswordException e, WebRequest req) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("비밀번호가 일치하지 않습니다.. "+ e.getMessage());
    }

    @ExceptionHandler(ValidationExceptions.class)
    public ResponseEntity<String> handlerValidationException(MethodArgumentNotValidException e, WebRequest req) {
        String error = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
