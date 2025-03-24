package com.example.todo.controller;

import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.TodoEntity;
import com.example.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequiredArgsConstructor //final필드에 대해 자동 생성자 만들어줌
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    /**
     * 모든 일정 조회 list.html
     */
    @GetMapping
    public String findAllList(Model model) {
        List<TodoResponseDto> todo = todoService.findAllList();
        model.addAttribute("todo", todo);
        return "list";  // list.html
    }
//    @GetMapping
//    public ModelAndView list(@RequestParam(value = "author", required = false) String author) throws Exception {
//        List<TodoResponseDto> todo;
//        ModelAndView modelAndView = new ModelAndView("list");
//        if (author != null &&  !author.trim().isEmpty()) {
//           todo = todoService.findByAuthor(author);
//        } else {
//            todo = todoService.findAllList();
//        }
//        modelAndView.addObject("list", todo);
//        modelAndView.addObject("author", author);
//        return modelAndView;
//    }

    /**
     * GET ID로 단건 조회 페이지
     * 특정 ID에 해당하는 일정 정보를 조회해 read.html로 반환
     */
    @GetMapping("/read")
    public String readTodo(@RequestParam int id, Model model) {
        TodoResponseDto todo = todoService.findById(id);
        model.addAttribute("todo", todo);
        return "read";
    }

    /*
     *GET 할일 등록 페이지
     * 새로운 일정 등록할 때 사용하는 register.html 반환
    */
    @GetMapping("/register")
    public String registerTodoList(Model model) {
        model.addAttribute("todo", new TodoRequestDto());
        return "register";  // register.html
    }

    /*
     * POST 할 일 등록 (처리)
     * 새로운 일정 등록 완료 후 목록으로 리다이렉트
     * @ModelAttribute로 요청 데이터를 바인딩
     */
    @PostMapping("/register")
    public String registerTodoList(@ModelAttribute TodoRequestDto requestDto) {
        todoService.registerTodoList(requestDto);
        return "redirect:/todo";
    }

/*
* GET 일정 수정 페이지
* */
    @GetMapping("/update/{id}")
    public String updateTodoList(@PathVariable int id, @RequestParam String password, Model model) {
        // 비밀번호 확인 로직 추가
        boolean passwordVerified = todoService.checkPassword(id, password);
        if (!passwordVerified) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/todo/" + id;  // 비밀번호가 틀리면 상세 페이지로 리다이렉트
        }

        // 비밀번호가 맞으면 수정 페이지로 이동
        TodoResponseDto todo = todoService.findById(id);
        model.addAttribute("todo", todo);
        return "update";
    }

    /*
    * POST 일정 수정
    * 수정된 정보를 저장하고 목록으로 리다이렉트
    * */
    @PostMapping("/update/{id}")
    public String updateTodoList(@PathVariable int id, @ModelAttribute TodoRequestDto requestDto) {
        todoService.updateTodoList(id, requestDto);
        return "redirect:/todo";  // 목록으로 리다이렉트
    }

    //삭제처리
    @PostMapping("/{id}/delete")
    public String deleteTodoList(@PathVariable int id, @RequestParam String password) {
        todoService.deleteTodoList(id, password);
        return "redirect:/todo";  // 목록으로 리다이렉트
    }

}



