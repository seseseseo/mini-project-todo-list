package com.example.todo.repository;

import com.example.todo.dto.PageRequestDto;
import com.example.todo.entity.TodoEntity;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    int registerTodoList(TodoEntity todoEntity, int authorId); //일정 생성
    int updateTodoList(TodoEntity todoEntity); // 일정 수정
    int deleteTodoList(int id, String password); //일정 삭제

    Optional<TodoEntity> findById(int id); //id로 단일 일정 조회/ 옵셔널로 감싸서 데이터가 없는 경우 처리
    List<TodoEntity> findByAuthor(String author); //작성자 이름으로 일정 조회, 중복자가 있을 수 있으니 리스트로

    List<TodoEntity> getList(PageRequestDto pageRequestDto);
    int getCount(PageRequestDto pageRequestDto);
}
