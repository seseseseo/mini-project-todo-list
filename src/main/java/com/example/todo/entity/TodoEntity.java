package com.example.todo.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TodoEntity {
    private Long id;
    private String title;
    private String description;
    private String author;
    private String password;
    private boolean completed;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}
