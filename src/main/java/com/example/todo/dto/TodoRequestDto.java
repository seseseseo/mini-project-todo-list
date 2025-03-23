package com.example.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TodoRequestDto {
    private String title;
    private String description;
    private String author;
    private String password;
    private boolean completed;
    private LocalDate dueDate;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

}
