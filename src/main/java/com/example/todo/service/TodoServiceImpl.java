package com.example.todo.service;

import com.example.todo.dto.PageRequestDto;
import com.example.todo.dto.PageResponseDto;
import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.AuthorEntity;
import com.example.todo.entity.TodoEntity;
import com.example.todo.exception.DataAccessException;
import com.example.todo.exception.PasswordException;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.exception.TodoSaveException;
import com.example.todo.repository.AuthorRepository;
import com.example.todo.repository.TodoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Log4j2
@Service
public class TodoServiceImpl implements TodoService{
    private final TodoRepository todoRepository;
    private final AuthorRepository authorRepository;
    public TodoServiceImpl(AuthorRepository authorRepository, TodoRepository todoRepository) {
        this.authorRepository = authorRepository;
        this.todoRepository = todoRepository;
    }
    /**
     * 일정 목록을 조회
     * @param pageRequestDto 페이징 요청 정보
     * @return 페이지 응답 DTO
     * @throws DataAccessException 데이터 조회 중 오류 발생 시
     */
    @Override
    public PageResponseDto<TodoRequestDto> getList(PageRequestDto pageRequestDto) {
        try {
            // DB에서 가져온 엔티티 목록
            List<TodoEntity> entityList = todoRepository.getList(pageRequestDto);
            log.info("조회된 엔티티 수:", entityList.size());
            // 데이터가 없는 경우

            // Entity -> DTO 변환
            List<TodoRequestDto> dtoList = entityList.stream()
                    .map(TodoRequestDto::new)
                    .collect(Collectors.toList());

            // 전체 데이터 개수 조회
            int total = todoRepository.getCount(pageRequestDto);
            log.info("전체 데이터 개수:", total);

            // 페이지 응답 객체 생성
            return PageResponseDto.<TodoRequestDto>withAll()
                    .dtoList(dtoList)
                    .total(total)
                    .pageRequestDto(pageRequestDto)
                    .build();
        } catch (Exception e) {
            log.error("데이터 조회 중 오류 발생:", e.getMessage());
            throw new DataAccessException("데이터 조회 중 오류가 발생했습니다.");
        }
    }


    /**
     * 기능 : 단건 조회
     * Optional 처리로 조회되지 않으면 예외 던지고 조회된 엔티티를 응답 DTO로 변환
     * @param id   조회할   Todo의 ID
     * @return 삭제된 행의 수
     * @throws TodoNotFoundException 해당 ID의 일정이 없을 경우
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
     * 기능 : 새로운 Todo를 등록합니다
     * 기능 : DTO에서 엔티티로 변환하여 저장
     * @param requestDto Todo를 생성하기 위한 요청 DTO
     * @return 저장된 Todo의 ID
     * @throws TodoSaveException 일정 등록 중 오류 발생 시
     */
    @Override
    @Transactional
    public int registerTodoList(TodoRequestDto requestDto) {
        try {
            log.info("일정 등록 요청: {}", requestDto);
            // 1. 작성자 정보 저장 및 author_id 반환
            AuthorEntity authorEntity = AuthorEntity.builder()
                    .authorName(requestDto.getAuthorName())
                    .email(requestDto.getEmail())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            int authorId = authorRepository.saveAuthor(authorEntity);

            //2. 할 일 생성 엔티티
            TodoEntity todoEntity = TodoEntity.builder()
                    .title(requestDto.getTitle())
                    .description(requestDto.getDescription())
                    .authorId(authorId)
                    .password(requestDto.getPassword())
                    .completed(false)
                    .dueDate(requestDto.getDueDate())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .authorName(requestDto.getAuthorName())  // 💡 추가
                    .email(requestDto.getEmail())  // 💡 추가
                    .build();


            // 3. 할 일 등록
            int todoId = todoRepository.registerTodoList(todoEntity, authorId);
            log.info("할 일 등록 성공 : ", todoId);

            return todoId;
        }catch (Exception e) {
            log.error("할 일 등록 중 오류 발생 : ", e.getMessage());
            throw new TodoSaveException("할 일 등록 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 기능 :  Todo를 수정
     * @param id        수정할 Todo의 ID
     * @param requestDto 수정 요청 DTO
     * @return 수정된 Todo의 ID
     * @throws TodoNotFoundException 해당 ID의 Todo를 찾을 수 없는 경우
     * @throws IllegalArgumentException 비밀번호가 일치하지 않는 경우
     */
    @Override
    public int updateTodoList(int id, TodoRequestDto requestDto) throws TodoNotFoundException {
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("일정을 찾을 수 없습니다"));

        if(!todo.getPassword().equals(requestDto.getPassword())){
            throw new TodoNotFoundException("비밀번호가 일치하지 않습니다");
        }

        todo.setTitle(requestDto.getTitle());
        todo.setDescription(requestDto.getDescription());
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
            throw new PasswordException("비밀번호가 일치하지 않습니다");
        }
        return todoRepository.deleteTodoList(id, password);
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
        log.info("입력된 비밀번호: " + password );
        log.info("DB저장된 비밀번호: " + todo.getPassword());
        log.info("비밀번호 일치 여부: " + todo.getPassword().equals(password));

        return todo.getPassword().equals(password);
    }

    @Override
    public int getCount(PageRequestDto pageRequestDto) {
        return 0;
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
                .description(entity.getDescription())
                .dueDate(entity.getDueDate())
                .completed(entity.isCompleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .authorName(entity.getAuthorName())
                .build();
    }
}

