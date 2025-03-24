package com.example.todo.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TodoRequestDto {
    private int id;
    private String title;
    private String description;
    //private String author;
    private String password;
    private boolean completed;
    private LocalDate dueDate;
    private LocalDateTime updatedAt = LocalDateTime.now();;
    private LocalDateTime createdAt = LocalDateTime.now();;
    private int authorId;
    private String authorName;
    private String email;

}
