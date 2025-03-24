package com.example.todo.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TodoResponseDto {
    //password는 제외
    private int id;
    private String title;
    private String description;
    //private String author;
    private LocalDate dueDate;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int authorId;
    private String authorName;
    private String email;
}
