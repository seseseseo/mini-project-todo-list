package com.example.todo.repository;

import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.TodoEntity;
import com.example.todo.exception.TodoNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //생성자 주입
    public TodoRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("todo")
                .usingGeneratedKeyColumns("id")
                .usingColumns("title", "author", "description", "password", "dueDate", "completed");

        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    //JDBC를 사용해 db 로 조회한 결과 resultset을 todoentity 객체로 매핑하는 역할
    // 쿼리 작업을 직접 todo객체로 변환하는 작업이 필요함
    private RowMapper<TodoEntity> todoEntityRowMapper = (rs, rowNum) -> TodoEntity.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .description(rs.getString("description"))
            .author(rs.getString("author"))
            .password(rs.getString("password"))
            .completed(rs.getBoolean("completed"))
            .dueDate(rs.getTimestamp("dueDate").toLocalDateTime().toLocalDate())
            .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updatedAt").toLocalDateTime())
            .build();
//entity생성자를 추가해주니까 오류가 떴는데

    @Override
    public int saveTodo(TodoEntity todoEntity) {
        String sql = "INSERT INTO schedule (title, author, description, password, createdAt, updatedAt, dueDate, completed) " +
                "VALUES (:title, :author, :description, :password, :createdAt, :updatedAt, :dueDate, :completed)";
        // 나눠주신 강의에서는 jdbc templete를 사용했지만 계속 순서 오류가 나는 문제가 힘들어서 다른 방법을 찾아봄
        //  순서가 섞이이는 문제로 오류 나는게 싫어서 다른 방법을 찾아봄
        Map<String, Object> params = new HashMap<>();
        params.put("title", todoEntity.getTitle());
        params.put("author", todoEntity.getAuthor());
        params.put("password", todoEntity.getPassword());
        params.put("description", todoEntity.getDescription());
        params.put("createdAt", Timestamp.valueOf(todoEntity.getCreatedAt()));
        params.put("updatedAt", Timestamp.valueOf(todoEntity.getUpdatedAt()));
        params.put("dueDate", Timestamp.valueOf(todoEntity.getDueDate().atStartOfDay()));
        params.put("completed", todoEntity.isCompleted());
        //insert시 자동으로 .키 생성
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 순서 무관하게 바인딩
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);
        return keyHolder.getKey().intValue();

    }

    //
    @Override
    public int updateTodo(TodoEntity todoEntity) {
        String sql = "UPDATE TODO SET title = :title, author = :author, updatedAt = :updatedAt, " +
                "dueDate = :dueDate, description = :description, completed = :completed WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", todoEntity.getTitle());
        params.addValue("author", todoEntity.getAuthor());
        params.addValue("updatedAt", Timestamp.valueOf(todoEntity.getUpdatedAt()));
        params.addValue("dueDate", Timestamp.valueOf(todoEntity.getDueDate().atStartOfDay()));
        params.addValue("description", todoEntity.getDescription());
        params.addValue("completed", todoEntity.isCompleted());
        params.addValue("id", todoEntity.getId());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int deleteTodo(int id, String password) throws TodoNotFoundException{
        String sql = "DELETE FROM todo WHERE id = ?";
        int result = jdbcTemplate.update(sql, id);
        return result;
    }

    @Override
    public TodoEntity findById(int id) throws TodoNotFoundException {
        String sql = "SELECT * FROM todo WHERE id = ?";
        List<TodoEntity> results = jdbcTemplate.query(sql, todoEntityRowMapper, id);
        return results.get(0);
    }

    @Override
    public List<TodoEntity> findByAuthor(String author) {
        String sql = "SELECT * FROM todo WHERE author = ?";
        return jdbcTemplate.query(sql, todoEntityRowMapper, author);
    }

    @Override
    public List<TodoEntity> findAllList() {
        String sql = "select * from todo order by updateAt desc ";
        return jdbcTemplate.query(sql, todoEntityRowMapper);

    }

    @Override
    public List<TodoEntity> findByTitle(String title) {
        String sql = "select from todo where title = ?";
        List<TodoEntity> results = jdbcTemplate.query(sql, todoEntityRowMapper, title);
        return results; // 이거는... 리스트를 . 반환해야하나?
    }

}
