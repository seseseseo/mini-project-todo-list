package com.example.todo.repository;

import com.example.todo.dto.TodoResponseDto;
import com.example.todo.entity.TodoEntity;
import com.example.todo.exception.DataAccessException;
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
import java.util.*;

/**
 * TodoRepositoryImpl 클래스는 TodoRepository 인터페이스를 구현하여
 * 데이터베이스와 상호작용하는 구체적인 로직을 제공합니다.
 * JDBC와 NamedParameterJdbcTemplate을 사용하여 데이터베이스 작업을 수행합니다.
 */
@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    /**
     * 생성자를 통해 JdbcTemplate과 NamedParameterJdbcTemplate을 주입받습니다.
     *
     * @param jdbcTemplate               JdbcTemplate 인스턴스
     * @param namedParameterJdbcTemplate NamedParameterJdbcTemplate 인스턴스
     */
    public TodoRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("todo")
                .usingGeneratedKeyColumns("id")
                .usingColumns("title", "author", "description", "password", "dueDate", "completed");

        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    /**
     *   (JDBC를 사용해 db 로 조회한 결과( resultset ) 을  todoentity 객체로 매핑하는 역할)
     *   TodoEntity를 매핑하는 RowMapper입니다.
     *   ResultSet으로부터 데이터를 추출하여 TodoEntity 객체를 생성합니다.
     */
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
            .build();//entity생성자를 추가해주니까 오류가 떠버림 왤까?


    /**
     * 새로운 Todo를 데이터베이스에 저장합니다.
     *
     * @param todoEntity를 저장할 TodoEntity 객체
     * @return 생성된 Todo의 ID
     */
    @Override
    public int saveTodo(TodoEntity todoEntity) {
        String sql = "INSERT INTO schedule (title, author, description, password, createdAt, updatedAt, dueDate, completed) " +
                "VALUES (:title, :author, :description, :password, :createdAt, :updatedAt, :dueDate, :completed)";

        Map<String, Object> params = new HashMap<>();
        params.put("title", todoEntity.getTitle());
        params.put("author", todoEntity.getAuthor());
        params.put("password", todoEntity.getPassword());
        params.put("description", todoEntity.getDescription());
        params.put("createdAt", Timestamp.valueOf(todoEntity.getCreatedAt()));
        params.put("updatedAt", Timestamp.valueOf(todoEntity.getUpdatedAt()));
        params.put("dueDate", Timestamp.valueOf(todoEntity.getDueDate().atStartOfDay()));
        params.put("completed", todoEntity.isCompleted());

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder(); //insert시 자동으로 키 생성
            namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder); // 순서 무관하게 바인딩
            return keyHolder.getKey().intValue();
        } catch (DataAccessException e) {
            throw new DataAccessException("일정 저장 중 오류가 발생했습니다", e);
        }
    }

    /**
     *  Todo를 수정합니다.
     *
     * @param todoEntity : 수정할 TodoEntity 객체
     * @return 수정된 행의 수를 리턴합니다.
     */
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

        try {
            return namedParameterJdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            throw new DataAccessException("일정 수정 중 오류가 발생했습니다.",e);
        }
    }

    /**
     * 특정 ID의 Todo를 삭제합니다.
     *
     * @param id : 삭제할 Todo의 ID
     * @param password : Todo 비밀번호
     * @return 삭제된 행의 수
     */
    @Override
    public int deleteTodo(int id, String password){
        String sql = "DELETE FROM todo WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            throw new DataAccessException("일정 삭제 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * ID를 통해 단일 Todo를 조회합니다.
     *
     * @param id : 조회할 Todo의 ID
     * @return 조회된 TodoEntity 객체를 감싼 Optional 객체
     */
    @Override
    public Optional<TodoEntity> findById(int id)  {
        String sql = "SELECT * FROM todo WHERE id = ?";
        List<TodoEntity> results = jdbcTemplate.query(sql, todoEntityRowMapper, id);
        return Optional.of(results.isEmpty() ? null : results.get(0));
    }

    /**
     * 작성자를 통해 단일 Todo를 조회합니다.
     *
     * @param author : 조회할 작성자
     * @return 저자가 작성한 Todo목록
     */
    @Override
    public List<TodoEntity> findByAuthor(String author) {
        String sql = "SELECT * FROM todo WHERE author = ?";
        return jdbcTemplate.query(sql, todoEntityRowMapper, author);
    }

    /**
     * 전체 Todo 목록을 최신순으로 조회합니다.
     *
     * @return 전체 Todo 목록
     */
    @Override
    public List<TodoEntity> findAllList() {
        String sql = "select * from todo order by updateAt desc ";
        try {
            return jdbcTemplate.query(sql, todoEntityRowMapper);
        } catch (DataAccessException e) {
            throw new DataAccessException("전체 목록 조회 중 오류가 발생했습니다.", e);
        }

    }

    /**
     * 제목을 통해 Todo 목록을 조회합니다.
     *
     * @param title 조회할 제목
     * @return 해당 제목의 Todo 목록
     */
    @Override
    public List<TodoEntity> findByTitle(String title) {
        String sql = "select from todo where title = ?";
        try {
            return jdbcTemplate.query(sql, todoEntityRowMapper, title);
        } catch (DataAccessException e) {
            throw new DataAccessException("제목으로 일정 조회 중 오류가 발생했습니다.", e);
        }
    }

}
