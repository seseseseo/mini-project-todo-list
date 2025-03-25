package com.example.todo;

import com.example.todo.entity.TodoEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class TodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
        TodoEntity todoEntity = new TodoEntity();
        System.out.println("Title: " + todoEntity.getTitle());
    }

}
