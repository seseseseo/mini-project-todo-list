package com.example.todo.dto;

import com.example.todo.entity.TodoEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TodoRequestDto {
    private int id;
    private String title;
    private String description;
    private String password;
    private boolean completed;
    private LocalDate dueDate;
    private LocalDateTime updatedAt = LocalDateTime.now();;
    private LocalDateTime createdAt = LocalDateTime.now();;

    private int authorId;
    private String authorName;
    private String email;
    public TodoRequestDto(TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.password = entity.getPassword();
        this.completed = entity.isCompleted();
        this.dueDate = entity.getDueDate();
        this.updatedAt = entity.getUpdatedAt();
        this.createdAt = entity.getCreatedAt();
        this.authorId = entity.getAuthorId();
        this.authorName = entity.getAuthorName();
        this.email = entity.getEmail();

    }


}
