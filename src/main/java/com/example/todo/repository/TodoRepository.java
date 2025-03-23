package com.example.todo.repository;

import com.example.todo.entity.TodoEntity;
import com.example.todo.exception.TodoNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    int saveTodo(TodoEntity todoEntity); //일정 생성
    int updateTodo(TodoEntity todoEntity); // 일정 수정
    int deleteTodo(int id, String password)throws TodoNotFoundException; //일정 삭제

    //Optional로 감싸면 ID를 찾지 못할 경우를 대비한 것이다. id는 유일하기 때문에
    TodoEntity findById(int id)throws TodoNotFoundException; //id로 단일 일정 조회/ 단건 조회
    List<TodoEntity> findByAuthor(String author); //작성자 이름으로 일정 조회
    List<TodoEntity> findAllList(); // 전체 일정 조회(수정일 기준내림차순으로)
    List<TodoEntity> findByTitle(String title);// 근데 제목은 여러건이 있을 . 있으니 리스트로 반환함
}
