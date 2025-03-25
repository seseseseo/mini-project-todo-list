package com.example.todo.repository;

import com.example.todo.entity.AuthorEntity;

public interface AuthorRepository {
    int saveAuthor(AuthorEntity authorEntity);
    AuthorEntity findAuthorById(int id);
}
