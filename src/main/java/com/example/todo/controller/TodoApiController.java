package com.example.todo.controller;

import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoApiController {
    private final TodoService todoService;

     /*
     *  일정 등록 처리 (POST /api/todo)
     *
     */
    @PostMapping
    public ResponseEntity<String> registerTodoList(@RequestBody TodoRequestDto requestDto){
        todoService.registerTodoList(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("일정이 성공적으로 추가되었습니다." + requestDto.toString());
    }

    //단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> findById(@PathVariable int id) {

        TodoResponseDto todo = todoService.findById(id);
        return ResponseEntity.ok(todo);
    }
    //전체조회
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> findAllList() {
        List<TodoResponseDto> todo = todoService.findAllList();
        return ResponseEntity.ok(todo);
    }
    /**
     * 일정 수정 처리
     */

    @PostMapping("/update/{id}")
    public String updateTodoList(@PathVariable int id, @ModelAttribute TodoRequestDto requestDto) {
        todoService.updateTodoList(id, requestDto);
        return "redirect:/todo";  // 수정 후 목록 페이지로 리다이렉트
    }


    /**
     * 일정 삭제 처리
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodoList(@PathVariable int id, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        todoService.deleteTodoList(id, password);
        return ResponseEntity.ok("일정이 삭제되었습니다.");
    }

    //비밀번호 체크
    @PostMapping("/check-password/{id}")
    public ResponseEntity<String> checkPassword(@PathVariable int id, @RequestBody Map<String, String> request) {
        System.out.println("checkPassword 호출됨. ID: " + id);
        System.out.println("요청 바디: " + request);
        String password = request.get("password");

        if (password == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 불일치");
        }
        boolean passwordVerified = todoService.checkPassword(id, password);
        if (!passwordVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }
        return ResponseEntity.ok("비밀번호 일치");


    }
}
