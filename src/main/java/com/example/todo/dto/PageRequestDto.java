package com.example.todo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
@Log4j2
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDto {
    private int id;
    private String title;
    private LocalDate dueDate;
    private String authorName;
    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1;

    @Builder.Default
    @Min(value = 10)
    @Max(value = 100)
    @Positive
    private int size = 10;

    public int getSkip(){
        int skip = (page - 1) * size;
        log.info("Page: " + page + ", Size: " + size + ", Skip: " + skip);
        return skip;
    }
    public int getPage() {
        return page;
    }
    public int getSize() {
        return size;
    }
    @Builder
    public PageRequestDto(int page, int size) {
        this.page = (page > 0) ? page : 1;
        this.size = (size > 0) ? size : 10;
    }

    private String searchType;
    private String query;
}
