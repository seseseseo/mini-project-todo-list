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
    private String author;
    private String password;
    private boolean completed;

    private LocalDate dueDate = LocalDate.now();
    private LocalDateTime createdAt = LocalDateTime.now();;
    private LocalDateTime updatedAt = LocalDateTime.now();;

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public void update(String title, String author, LocalDateTime updatedAt) {
        this.title = title;
        this.author = author;
        this.updatedAt = updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
