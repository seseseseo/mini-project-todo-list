package com.example.todo.repository;

import com.example.todo.entity.AuthorEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Objects;

@Slf4j
@Repository
@AllArgsConstructor
public class AuthorRepositoryImpl implements AuthorRepository {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public int saveAuthor(AuthorEntity authorEntity) {
        String authorSql = "insert into author (authorName, email, createdAt, updatedAt) " +
                "values (:authorName, :email, :createdAt, :updatedAt)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("authorName", authorEntity.getAuthorName() != null ? authorEntity.getAuthorName() : "unknown");
        params.addValue("email", authorEntity.getEmail() != null ? authorEntity.getEmail() : "Noooo-email@example.com");
        params.addValue("createdAt", Timestamp.valueOf(authorEntity.getCreatedAt()));
        params.addValue("updatedAt", Timestamp.valueOf(authorEntity.getUpdatedAt()));

        //자동 생성 되는 키
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            int rowsAffected = namedParameterJdbcTemplate.update(authorSql, params, keyHolder, new String[]{"author_id"});
            Number key = keyHolder.getKey();
            if (key == null) {
                throw new RuntimeException("Generated Key is null");
            }
            int generatedId = key.intValue();
            return generatedId;
        } catch (DataAccessException e) {
            System.err.println("작성자 저장 중에 문제 발생");
            e.printStackTrace();
            throw new com.example.todo.exception.DataAccessException("작성자 데이터 저장 중에 문제 발생");
        }

    }


    @Override
    public AuthorEntity findAuthorById(int id) {
        String sql = "select * from author where author_id = :author_id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("author_id", id);

        return namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> AuthorEntity.builder()
                .authorId(rs.getInt("author_id"))
                .authorName(rs.getString("authorName"))
                .email(rs.getString("email"))
                .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                .build());
    }



}
