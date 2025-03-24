package com.example.todo.service;

import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.TodoEntity;

import java.util.List;

public interface TodoService {
    int registerTodoList(TodoRequestDto requestDto);
    int updateTodoList(int id, TodoRequestDto requestDto);
    int deleteTodoList(int id, String password);
   // TodoResponseDto findById(int id);
    TodoResponseDto findById(int id);
    List<TodoResponseDto> findAllList();
    List<TodoResponseDto> findByAuthor(String author); // 작성자
    boolean completedTodo(int id);
    boolean checkPassword(int id, String password);
}
