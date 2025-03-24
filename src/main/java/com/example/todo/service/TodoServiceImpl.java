package com.example.todo.service;

import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.TodoEntity;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.repository.TodoRepository;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
@Log4j2
@Service
public class TodoServiceImpl implements TodoService{
    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * 모든 Todo 목록을 조회
     * @return 전체 Todo 목록의 응답 DTO 리스트
     */
    @Override
    public List<TodoResponseDto> findAllList() {
        List<TodoEntity> entities = todoRepository.findAllList();
        return entities.stream().map(this::toResponseDto).collect(Collectors.toList());
    }
    /**
     * 기능 : 단건 조회
     * Optional 처리로 조회되지 않으면 예외 던지고 조회된 엔티티를 응답 DTO로 변환
     * @param id        Todo의 ID
     * @return 삭제된 행의 수
     * @throws TodoNotFoundException 조회되지 않으면 예외 던짐
     */
    @Override
    public TodoResponseDto findById(int id) {
        System.out.println("Received ID: " + id); // 디버그 로그 추가
        Optional<TodoEntity> optionalTodo = todoRepository.findById(id);
        TodoEntity todoEntity = optionalTodo.orElseThrow(() -> new TodoNotFoundException("해당 ID의 일정이 존재하지 않습니다."));

        System.out.println("조회된 일정 ID: " + todoEntity.getId());
        return toResponseDto(todoEntity);
    }
    /**
     * 기능 : 새로운 Todo를 저장합니다.
     * 기능 : DTO에서 엔티티로 변환하여 저장
     *
     * @param requestDto Todo를 생성하기 위한 요청 DTO
     * @return 저장된 Todo의 ID
     */
    @Override
    public int registerTodoList(TodoRequestDto requestDto) {
        System.out.println("title: " + requestDto.getTitle());
        TodoEntity todoEntity = TodoEntity.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                //.author(requestDto.getAuthor())
                .password(requestDto.getPassword())
                .completed(false)
                .dueDate(requestDto.getDueDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return todoRepository.registerTodoList(todoEntity);
    }

    /**
     * 기능 :  Todo를 수정
     * ID로 Todo를 조회하여 수정할 필드를 업데이트하고 저장합니다.
     * @param id        수정할 Todo의 ID
     * @param requestDto 수정할 필드 정보를 담고 있는 요청 DTO
     * @return 수정된 Todo의 ID
     * @throws TodoNotFoundException 해당 ID의 Todo를 찾을 수 없는 경우
     * @throws IllegalArgumentException 비밀번호가 일치하지 않는 경우
     */
    @Override
    public int updateTodoList(int id, TodoRequestDto requestDto) throws TodoNotFoundException {
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("일정을 찾을 수 없습니다"));
        //db에서 todo를 조회해 Optional로 감쌈
        if(!todo.getPassword().equals(requestDto.getPassword())){
            throw new TodoNotFoundException("비밀번호가 일치하지 않습니다");
        }
        todo.setTitle(requestDto.getTitle());
        todo.setDescription(requestDto.getDescription());
        //todo.setAuthor(requestDto.getAuthor());
        todo.setCompleted(requestDto.isCompleted());
        todo.setDueDate(requestDto.getDueDate());
        todo.setUpdatedAt(LocalDateTime.now());

        return todoRepository.updateTodoList(todo);

    }

    /**
     * 기능 : Todo를 삭제
     * 기능 : 비밀번호를 검증하여 삭제
     * @param id       삭제할 Todo의 ID
     * @param password 삭제할 Todo의 비밀번호
     * @return 삭제된 행의 수
     * @throws TodoNotFoundException 해당 ID의 Todo를 찾을 수 없는 경우
     * @throws IllegalArgumentException 비밀번호가 일치하지 않는 경우
     */
    @Override
    public int deleteTodoList(int id, String password) {
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("일정을 찾을 수 없습니다"));
        if(!todo.getPassword().equals(password)){
            throw new TodoNotFoundException("비밀번호가 일치하지 않습니다");
        }
        return todoRepository.deleteTodoList(id, password);
    }




//    @Override
//    public TodoResponseDto findById(int id) {
//        System.out.println("Received ID: " + id); // 디버그 로그 추가
//        Optional<TodoEntity> optionalTodo = todoRepository.findById(id);
//        TodoEntity todoEntity = optionalTodo.orElseThrow(() -> new NoSuchElementException("해당 ID의 일정이 존재하지 않습니다."));
//        return TodoResponseDto.builder()
//                .id(todoEntity.getId())
//                .title(todoEntity.getTitle())
//                .description(todoEntity.getDescription())
//                .author(todoEntity.getAuthor())
//                .completed(todoEntity.isCompleted())
//                .createdAt(todoEntity.getCreatedAt())
//                .updatedAt(todoEntity.getUpdatedAt())
//                .dueDate(todoEntity.getDueDate())
//                .build();
//    }



    /**
     * 기능:작성자 기준으로 Todo 목록을 조회합
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
     * @param id 완료 상태를 변경할 Todo의 ID
     * @return 변경된 완료 상태 (true: 완료, false: 미완료)
     * @throws TodoNotFoundException 해당 ID의 Todo를 찾을 수 없는 경우
     */
    @Override
    public boolean completedTodo(int id) {
        TodoEntity entity = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("일정을 찾을 수 없습니다. ID: " + id));
        entity.setCompleted(!entity.isCompleted());
        todoRepository.updateTodoList(entity);
        return entity.isCompleted();
    }

    /**
     * 비밀번호 검증
     * @param id, password
     * @return 비밀번호 검증값
     * @throws TodoNotFoundException 해당 ID의 Todo를 찾을 수 없는 경우
     */
    @Override
    public boolean checkPassword(int id, String password) {
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow( () -> new TodoNotFoundException("일정을 찾을 수 없습니다"));
        log.info("입력된 비밀번호: [" + password + "]");
        log.info("DB저장된 비밀번호 [" + todo.getPassword() + "]");
        log.info("비밀번호 일치 여부: " + todo.getPassword().equals(password));

        return todo.getPassword().equals(password);
    }

    /**
     * TodoEntity를 TodoResponseDto로 변환
     * @param entity 변환할 TodoEntity 객체
     * @return 변환된 TodoResponseDto 객체
     */
    private TodoResponseDto toResponseDto(TodoEntity entity) {
        return TodoResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                //.author(entity.getAuthor())
                .description(entity.getDescription())
                .dueDate(entity.getDueDate())
                .completed(entity.isCompleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

