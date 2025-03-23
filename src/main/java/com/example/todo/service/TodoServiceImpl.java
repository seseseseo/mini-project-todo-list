package com.example.todo.service;

import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.TodoEntity;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService{
    private final TodoRepository todoRepository;

    /**
     * TodoServiceImpl 생성자.
     * TodoRepository를 주입 받아 사용합니다.
     *
     * @param todoRepository Todo 데이터를 관리하는 레포지토리
     */
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    //서비스 계층에서 엔티티를 직접 생성하여 레포지토리로 전달
    /**
     * 새로운 Todo를 저장합니다.
     * 요청으로 전달된 데이터를 기반으로 TodoEntity를 생성하여 저장.
     *
     * @param requestDto Todo를 생성하기 위한 요청 DTO
     * @return 저장된 Todo의 ID
     */
    @Override
    public int saveTodo(TodoRequestDto requestDto) {
        TodoEntity todoEntity = TodoEntity.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .author(requestDto.getAuthor())
                .password(requestDto.getPassword())
                .completed(false)
                .dueDate(requestDto.getDueDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return todoRepository.saveTodo(todoEntity);
    }

    /**
     * 기존 Todo를 수정합니다.
     * ID로 Todo를 조회하여 수정할 필드를 업데이트하고 저장합니다.
     *
     * @param id        수정할 Todo의 ID
     * @param requestDto 수정할 필드 정보를 담고 있는 요청 DTO
     * @return 수정된 Todo의 ID
     * @throws TodoNotFoundException 해당 ID의 Todo를 찾을 수 없는 경우
     * @throws IllegalArgumentException 비밀번호가 일치하지 않는 경우
     */
    @Override
    public int updateTodo(int id, TodoRequestDto requestDto) throws TodoNotFoundException {
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("일정을 찾을 수 없습니다"));
        //db에서 todo를 조회해 Optional로 감쌈

        if(!todo.getPassword().equals(requestDto.getPassword())){
            throw new TodoNotFoundException("비밀번호가 일치하지 않습니다");
        }

        TodoEntity updatedTodo = TodoEntity.builder()
                //.title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .author(requestDto.getAuthor())
                //.password(todo.getPassword())
                .completed(requestDto.isCompleted())
                .dueDate(requestDto.getDueDate())
                //.createdAt(todo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        return todoRepository.updateTodo(updatedTodo);

    }

    /**
     * Todo를 삭제
     * ID와 비밀번호를 검증하여 삭제
     *
     * @param id       삭제할 Todo의 ID
     * @param password 삭제할 Todo의 비밀번호
     * @return 삭제된 행의 수
     * @throws TodoNotFoundException 해당 ID의 Todo를 찾을 수 없는 경우
     * @throws IllegalArgumentException 비밀번호가 일치하지 않는 경우
     */
    @Override
    public int deleteTodo(int id, String password) {
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("일정을 찾을 수 없습니다"));
        if(!todo.getPassword().equals(password)){
            throw new TodoNotFoundException("비밀번호가 일치하지 않습니다");
        }
        return todoRepository.deleteTodo(id, password);
    }

    /**
     * ID로 단일 Todo를 조회
     *
     * @param id 조회할 Todo의 ID
     * @return 조회된 Todo의 응답 DTO
     * @throws TodoNotFoundException 해당 ID의 Todo를 찾을 수 없는 경우
     */
    @Override
    public TodoResponseDto findById(int id) {
        Optional<TodoEntity> todoEntity = todoRepository.findById(id);
        return todoEntity.map(this::toResponseDto).orElseThrow(() -> new TodoNotFoundException("일정을 찾을 수 없습니다."));
    }

    /**
     * 모든 Todo 목록을 조회
     *
     * @return 전체 Todo 목록의 응답 DTO 리스트
     */
    @Override
    public List<TodoResponseDto> findAllList() {
        List<TodoEntity> entities = todoRepository.findAllList();
        return entities.stream().map(this::toResponseDto).collect(Collectors.toList());

    }

    /**
     * 저자명을 기준으로 Todo 목록을 조회합
     *
     * @param author 조회할 저자명
     * @return 해당 저자가 작성한 Todo 목록의 응답 DTO 리스트
     */
    @Override
    public List<TodoResponseDto> findByAuthor(String author) {
        List<TodoEntity> entities = todoRepository.findByAuthor(author);
        return entities.stream().map(this::toResponseDto).collect(Collectors.toList());

    }

    /**
     * Todo 완료 상태를 반전
     *
     * @param id 완료 상태를 변경할 Todo의 ID
     * @return 변경된 완료 상태 (true: 완료, false: 미완료)
     * @throws TodoNotFoundException 해당 ID의 Todo를 찾을 수 없는 경우
     */
    @Override
    public boolean completedTodo(int id) {
        TodoEntity entity = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("일정을 찾을 수 없습니다. ID: " + id));
        entity.setCompleted(!entity.isCompleted());
        todoRepository.updateTodo(entity);
        return entity.isCompleted();
    }

    /**
     * TodoEntity를 TodoResponseDto로 변환
     *
     * @param entity 변환할 TodoEntity 객체
     * @return 변환된 TodoResponseDto 객체
     */
    private TodoResponseDto toResponseDto(TodoEntity entity) {
        return TodoResponseDto.builder()
                //.id(String.valueOf(entity.getId()))
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .description(entity.getDescription())
                .dueDate(entity.getDueDate())
                .completed(entity.isCompleted())
                //.createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

