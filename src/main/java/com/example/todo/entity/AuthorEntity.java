package com.example.todo.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class AuthorEntity {
    private int authorId;
    private String authorName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
