package com.example.todo.service;

import com.example.todo.dto.PageRequestDto;
import com.example.todo.dto.PageResponseDto;
import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.TodoEntity;

import java.util.List;

public interface TodoService {
    int registerTodoList(TodoRequestDto requestDto);//삽입된 행 개수를 반환하기 위함
    int updateTodoList(int id, TodoRequestDto requestDto);
    int deleteTodoList(int id, String password);
    TodoResponseDto findById(int id); // 특정  ID를 이용해 할일을 조회하고, DTO올 반환;
    boolean completedTodo(int id);
    boolean checkPassword(int id, String password);
    PageResponseDto<TodoRequestDto> getList(PageRequestDto pageRequestDto);
    int getCount(PageRequestDto pageRequestDto);
}
