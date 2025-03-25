package com.example.todo.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TodoEntity {

    private int id;
    private String title;
    private String description;
    private String password;
    private boolean completed = false;
    private int authorId; // FK
    private String authorName;
    private String email;

    private LocalDate dueDate = LocalDate.now();
    private LocalDateTime createdAt = LocalDateTime.now();;
    private LocalDateTime updatedAt = LocalDateTime.now();;



    public void update(String title, int authorId, LocalDateTime updatedAt) {
        this.title = title;
        this.authorId = authorId;
        this.updatedAt = updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
