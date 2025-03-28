package com.example.todo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class PageResponseDto<E> {
    private int id;
    private String title;
    private LocalDate dueDate;
    private String authorName;
    private int page; // 현재 페이지
    private int size; // 페이지 크기
    private int total; // 전체 데이터 수
    private boolean completed = false;

    private int start;  //시작 페이지 번호
    private int end;// 끝 페이지

    private boolean prev; // 이전 버튼 활성화 여부
    private boolean next;// 다음 버튼 활성화 여부
    // 검색 관련 필드
    private String searchType;
    private String query;
    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDto(PageRequestDto pageRequestDto, List<E> dtoList, int total) {
        this.page = pageRequestDto.getPage();
        this.size = pageRequestDto.getSize();
        this.total = total;
        this.dtoList = dtoList;
        // 총 페이지 계산 (전체 데이터 수와 페이지 크기를 이용)
        int last = (int) Math.ceil((double) total / size);

        // 끝 페이지는 10개씩 묶어서 계산
        this.end = (int) Math.ceil((double) page / 10) * 10;
        this.start = this.end - 9;

        // 마지막 페이지가 end보다 작다면 end를 마지막 페이지로 수정
        this.end = Math.min(this.end, last);

        // 이전 페이지 버튼 활성화 여부
        this.prev = this.start > 1;

        // 다음 페이지 버튼 활성화 여부
        this.next = total > this.end * this.size;
    }
}
