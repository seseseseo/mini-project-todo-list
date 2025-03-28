package com.example.todo.controller;

import com.example.todo.dto.PageRequestDto;
import com.example.todo.dto.PageResponseDto;
import com.example.todo.dto.TodoRequestDto;
import com.example.todo.dto.TodoResponseDto;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor //final필드에 대해 자동 생성자 만들어줌
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    /**
     * 모든 일정 조회 list.html
     */
    @GetMapping
    public String getList(@Valid PageRequestDto pageRequestDto,
                          @RequestParam(value = "searchType", required = false) String searchType,
                          @RequestParam(value = "query", required = false) String query,
                          BindingResult bindingResult, Model model) {

        log.info("페이지 요청: {}", pageRequestDto);
        log.info("검색 유형: {}", searchType);  // 💡 로그 추가
        log.info("검색어: {}", query);
        if (bindingResult.hasErrors()) {
            pageRequestDto = PageRequestDto.builder().build();
        }
        //조회가안되길래 이거 추가
        if ((searchType == null || searchType.isEmpty()) && (query == null || query.trim().isEmpty())) {
            log.info("검색어가 없다..");
            PageResponseDto<TodoRequestDto> responseDto = todoService.getList(pageRequestDto);

            model.addAttribute("responseDTO", responseDto.getDtoList());
            model.addAttribute("currentPage", responseDto.getPage());
            model.addAttribute("total", responseDto.getTotal());
            model.addAttribute("size", responseDto.getSize());
            model.addAttribute("endPage", responseDto.getEnd());
            model.addAttribute("startPage", responseDto.getStart());
            model.addAttribute("prev", responseDto.isPrev());
            model.addAttribute("next", responseDto.isNext());


            return "list";
        }

        //  검색 유형  검색어 설정
        pageRequestDto.setSearchType(searchType);
        pageRequestDto.setQuery(query);

        // 검색 유형에 따라 DTO 필드 설정
        if ("authorName".equalsIgnoreCase(searchType)) {
            pageRequestDto.setAuthorName(pageRequestDto.getAuthorName());
        } else if ("title".equalsIgnoreCase(searchType)) {
            pageRequestDto.setTitle(pageRequestDto.getTitle());
        } else {
            log.warn("유효하지 않은 검색 유형: {}", searchType);
            return "redirect:/todo";
        }

        // 서비스에서 검색 결과 가져오기
        PageResponseDto<TodoRequestDto> responseDto;

        if ("authorName".equalsIgnoreCase(searchType)) {
            responseDto = todoService.searchByAuthor(pageRequestDto);
        } else if ("title".equalsIgnoreCase(searchType)) {
            responseDto = todoService.searchByTitle(pageRequestDto);
        } else {
            responseDto = todoService.getList(pageRequestDto);
        }
        //그거 .... 총 페이지 수

        model.addAttribute("responseDTO", responseDto.getDtoList());
        model.addAttribute("currentPage", responseDto.getPage());
        model.addAttribute("total", responseDto.getTotal());
        model.addAttribute("size", responseDto.getSize());
        model.addAttribute("endPage", responseDto.getEnd());
        model.addAttribute("startPage", responseDto.getStart());
        model.addAttribute("prev", responseDto.isPrev());
        model.addAttribute("next", responseDto.isNext());
        model.addAttribute("searchType", searchType);
        model.addAttribute("query", query);
        model.addAttribute("end",responseDto.getEnd());
        log.info("검색 결과 페이지로 이동합니다. 데이터 개수: ", responseDto.getDtoList().size());
        return "list";
    }
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



    @PostMapping
    public String searchByAuthorName(@RequestParam("authorName") String authorName) {
        try {
            // URL 인코딩 처리
            String encodedAuthorName = URLEncoder.encode(authorName, "UTF-8");
            return "redirect:/todo?authorName=" + encodedAuthorName;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "redirect:/todo";
        }
    }
     /*GET 할일 등록 페이지
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



