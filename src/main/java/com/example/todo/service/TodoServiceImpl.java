package com.example.todo.service;

import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.TodoEntity;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService{
    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    //서비스 계층에서 엔티티를 직접 생성하여 레포지토리로 전달
    @Override
    public int saveTodo(TodoRequestDto requestDto) {
        TodoEntity todoEntity = TodoEntity.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .author(requestDto.getAuthor())
                .password(requestDto.getPassword())
                .completed(false)
                .dueDate(requestDto.getDueDate())
                .build();

        return todoRepository.saveTodo(todoEntity);
    }

    @Override
    public int updateTodo(int id, TodoRequestDto requestDto) throws TodoNotFoundException {
        TodoEntity exist = todoRepository.findById(id);
        if (exist == null) {
            throw new TodoNotFoundException("일정 못찾음 ");
        }
        if(!exist.getPassword().equals(requestDto.getPassword())){
            throw new TodoNotFoundException("비밀번호 불일치");
        }
        exist.setTitle(requestDto.getTitle());
        exist.setDescription(requestDto.getDescription());
        exist.setAuthor(requestDto.getAuthor());
        exist.setDueDate(requestDto.getDueDate());
        exist.setUpdatedAt(LocalDateTime.now());

        return todoRepository.updateTodo(exist);

    }


    @Override
    public int deleteTodo(int id, String password) {

    }

    @Override
    public TodoResponseDto findById(int id) {
        return null;
    }

    @Override
    public List<TodoResponseDto> findAllList() {
        return List.of();
    }

    @Override
    public List<TodoResponseDto> findByAuthor(String author) {
        return List.of();
    }

    @Override
    public boolean completedTodo(int id) {
        return false;
    }
}
