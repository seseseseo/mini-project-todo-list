package com.example.todo.service;

import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.TodoEntity;

import java.util.List;

public interface TodoService {
    int saveTodo(TodoRequestDto requestDto);
    int updateTodo(int id, TodoRequestDto requestDto);
    int deleteTodo(int id, String password);
    TodoResponseDto findById(int id);
    List<TodoResponseDto> findAllList();
    List<TodoResponseDto> findByAuthor(String author); // 작성자
    boolean completedTodo(int id);
}
