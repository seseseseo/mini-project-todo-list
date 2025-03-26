package com.example.todo.dto;

import com.example.todo.entity.TodoEntity;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "할일은 필수 입력값입니다.")
    @Size(max = 200, message = "할일은 최대 200자 이내로 작성해야 합니다.")
    private String title;

    private String description;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "비밀번호는 8자 이상 20자 이하의 영문자와 숫자를 포함해야 합니다.")
    private String password;

    private boolean completed;
    private LocalDate dueDate;
    private LocalDateTime updatedAt = LocalDateTime.now();;
    private LocalDateTime createdAt = LocalDateTime.now();;

    private int authorId;
    private String authorName;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
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
