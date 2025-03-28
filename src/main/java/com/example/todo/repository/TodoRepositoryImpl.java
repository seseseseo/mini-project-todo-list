package com.example.todo.repository;


import com.example.todo.dto.PageRequestDto;
import com.example.todo.entity.AuthorEntity;
import com.example.todo.entity.TodoEntity;
import com.example.todo.exception.DataAccessException;

import com.example.todo.exception.TodoNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * TodoRepositoryImpl 클래스는 TodoRepository 인터페이스를 구현하여
 * 데이터베이스와 상호작용하는 구체적인 로직을 제공합니다.
 * JDBC와 NamedParameterJdbcTemplate을 사용하여 데이터베이스 작업을 수행합니다.
 */
@Log4j2
@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TodoRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     *   (JDBC를 사용해 db 로 조회한 결과( resultset ) 을  todoentity 객체로 매핑하는 역할)
     *   TodoEntity를 매핑하는 RowMapper
     *   ResultSet으로부터 데이터를 추출하여 TodoEntity 객체를 생성
     */
    private RowMapper<TodoEntity> todoEntityRowMapper = (rs, rowNum) -> TodoEntity.builder()
            .id(rs.getInt("id"))
            .title(rs.getString("title"))
            .completed(rs.getBoolean("completed"))
            .authorName(rs.getString("authorName"))
            .dueDate(rs.getTimestamp("dueDate") != null ? rs.getTimestamp("dueDate").toLocalDateTime().toLocalDate() : null)
            .createdAt(rs.getTimestamp("createdAt") != null ? rs.getTimestamp("createdAt").toLocalDateTime() : null)
            .updatedAt(rs.getTimestamp("updatedAt") != null ? rs.getTimestamp("updatedAt").toLocalDateTime() : null)
            .build();

    /**
     * 전체 Todo 목록을 최신순으로 조회
     * 페이징을 적용
     * @param pageRequestDto 페이지 요청 정보
     * @return 전체 Todo 목록
     */
    @Override
    public List<TodoEntity> getList(PageRequestDto pageRequestDto) {
        int page = pageRequestDto.getPage() - 1;
        int skip = pageRequestDto.getSkip();
        int size = pageRequestDto.getSize();

        StringBuilder sql = new StringBuilder("select t.id, t.title, t.createdAt, t.updatedAt, t.completed, a.authorName, t.dueDate ");
        sql.append("from todo t ");
        sql.append("join author a ON t.author_id = a.author_id ");  // author 테이블과 조인

        MapSqlParameterSource params = new MapSqlParameterSource();
        if(pageRequestDto.getAuthorName() != null && !pageRequestDto.getAuthorName().isEmpty()) {
            sql.append("where a.authorName like :authorName ");
            params.addValue("authorName", "%" + pageRequestDto.getAuthorName() + "%");
        }
        sql.append("order by ");
        sql.append("t.completed ASC, ");  // 미완료가 먼저, 완료가 나중에
        sql.append("case when date(coalesce( t.dueDate, Date(now()))) < Date(now()) then 1 else 0 end ASC, ");  // 마감 기한이 지난 것은 뒤로
        sql.append("t.dueDate asc ");  // 마감 기한이 가까운 순으로 정렬
        sql.append("limit ").append(size).append(" offset ").append(skip);


        params.addValue("skip", skip);
        params.addValue("size", size);

        return namedParameterJdbcTemplate.query(sql.toString(), params, todoEntityRowMapper);
    }

    /**
     * 작성자를 통해 단일 Todo를 조회
     * @param authorName : 조회할 작성자
     * @return 저자가 작성한 Todo목록
     */
    @Override
    public List<TodoEntity> findByAuthor(String authorName) {
        String sql = "select t.id, t.title, t.description, t.createdAt, t.updatedAt, t.completed, t.dueDate , a.authorName, a.email " +
                "from todo t " +
                "left join author a on t.author_id = a.author_id " +
                "where a.authorName like :authorName " +
                "order by t.createdAt desc";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("authorName", "%" + authorName + "%");
        return namedParameterJdbcTemplate.query(sql, params, todoEntityRowMapper);
    }

    /**
     *  할일 등록 새로운 Todo를 데이터베이스에 저장
     * @param todoEntity 를 저장할 TodoEntity 객체
     * @param authorId
     * @return 생성된 Todo의 ID
     */
    @Override
    public int registerTodoList(TodoEntity todoEntity, int authorId) {
        // 1. author 테이블에 데이터 삽입
        log.info("=== [디버그] TodoEntity 정보 ===");
        log.info("authorId = " + authorId);
        log.info("id: " + todoEntity.getId());
        log.info("Title: " + todoEntity.getTitle());
        log.info("AuthorName: " + todoEntity.getAuthorName());
        log.info("Email: " + todoEntity.getEmail());
        log.info("CreatedAt: " + todoEntity.getCreatedAt());
        log.info("UpdatedAt: " + todoEntity.getUpdatedAt());

        AuthorEntity authorEntity = AuthorEntity.builder()
                .authorName(todoEntity.getAuthorName())
                .email(todoEntity.getEmail())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        String sql = "INSERT INTO todo (title, description, password, createdAt, updatedAt, dueDate, completed, author_id) " +
                "VALUES (:title, :description, :password, :createdAt, :updatedAt, :dueDate, :completed, :author_id)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", todoEntity.getTitle());
        params.addValue("description", todoEntity.getDescription());
        params.addValue("password", todoEntity.getPassword());
        params.addValue("createdAt", Timestamp.valueOf(todoEntity.getCreatedAt()));
        params.addValue("updatedAt", Timestamp.valueOf(todoEntity.getUpdatedAt()));
        params.addValue("completed", todoEntity.isCompleted());
        params.addValue("dueDate", todoEntity.getDueDate() != null ? Timestamp.valueOf(todoEntity.getDueDate().atStartOfDay()) : null);
        params.addValue("author_id", authorId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            //insert시 자동으로 키 생성
            namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
            Number generatedKey = keyHolder.getKey();
            if (generatedKey == null) {
                throw new RuntimeException("Todo ID를 가져올 수 없습니다.");
            }
            int generatedId = generatedKey.intValue();
            return generatedId;
        } catch (DataAccessException e) {
            throw new DataAccessException("일정 저장 중 오류가 발생했습니다");
        }
    }


    /**
     *  Todo를 수정
     * @param todoEntity : 수정할 TodoEntity 객체
     * @return 수정된 행의 수를 리턴합니다.
     */
    @Override
    public int updateTodoList(TodoEntity todoEntity) {
        //1. 할 일 업데이트
        String sql = "update todo set title = :title, updatedAt = :updatedAt, " +
                "dueDate = :dueDate, description = :description, completed = :completed where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", todoEntity.getTitle());
        params.addValue("updatedAt", Timestamp.valueOf(todoEntity.getUpdatedAt()));
        params.addValue("dueDate", Timestamp.valueOf(todoEntity.getDueDate().atStartOfDay()));
        params.addValue("description", todoEntity.getDescription());
        params.addValue("completed", todoEntity.isCompleted());
        params.addValue("id", todoEntity.getId());

        int todoRows = namedParameterJdbcTemplate.update(sql, params);
        // 2. 작성자 업데이트
        String authorSql = "update author set authorName = :authorName, email = :email, updatedAt = :updatedAt " +
                "WHERE author_id = :author_id";
        MapSqlParameterSource authorParams = new MapSqlParameterSource()
                .addValue("authorName", todoEntity.getAuthorName())
                .addValue("email", todoEntity.getEmail())
                .addValue("updatedAt", Timestamp.valueOf(todoEntity.getUpdatedAt()))
                .addValue("author_id", todoEntity.getAuthorId());
        int authorRows = namedParameterJdbcTemplate.update(authorSql, authorParams);

        return authorRows;
    }

    /**
     * 특정 ID의 Todo를 삭제
     *
     * @param id : 삭제할 Todo의 ID
     * @param password : Todo 비밀번호
     * @return 삭제된 행의 수
     */
    @Override
    public int deleteTodoList(int id, String password){
        String sql = "DELETE FROM todo WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            int rows = namedParameterJdbcTemplate.update(sql, params);
            log.info("삭제 성공: {}", id);
            return rows;
        } catch (DataAccessException e) {
            log.error("삭제 중 오류: {}", id);
            throw new DataAccessException("삭제 중 오류가 발생했습니다.");
        }
    }

    /**
     * ID를 통해 단일 Todo를 조회
     *
     * @param id : 조회할 Todo의 ID
     * @return 조회된 TodoEntity 객체를 감싼 Optional 객체
     */
    @Override
    public Optional<TodoEntity> findById(int id)  {
        String sql = "select t.id, t.title, t.description, t.password, t.completed, t.createdAt, t.updatedAt, t.dueDate, " +
                "a.authorName, a.email " +
                "from todo t " +
                "join author a on t.author_id = a.author_id " +
                "where t.id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            TodoEntity entity = namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    TodoEntity.builder()
                            .id(rs.getInt("id"))
                            .title(rs.getString("title"))
                            .description(rs.getString("description"))
                            .password(rs.getString("password"))
                            .completed(rs.getBoolean("completed"))
                            .authorName(rs.getString("authorName"))
                            .email(rs.getString("email"))
                            .createdAt(rs.getTimestamp("createdAt") != null ? rs.getTimestamp("createdAt").toLocalDateTime() : null)
                            .updatedAt(rs.getTimestamp("updatedAt") != null ? rs.getTimestamp("updatedAt").toLocalDateTime() : null)
                            .dueDate(rs.getTimestamp("dueDate") != null ? rs.getTimestamp("dueDate").toLocalDateTime().toLocalDate() : null)
                            .build());

            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            log.info("데이터 조회 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("데이터 조회 중 오류가 발생했습니다.", e);
        }
    }



    @Override
    public int getCount(PageRequestDto pageRequestDto) {
        StringBuilder sql = new StringBuilder("select count(*) from todo t ");
        sql.append("join author a on t.author_id = a.author_id ");
        MapSqlParameterSource params = new MapSqlParameterSource();
        if(pageRequestDto.getAuthorName() != null && !pageRequestDto.getAuthorName().isEmpty()) {
            sql.append("where a.authorName like :authorName ");
            params.addValue("authorName", "%" + pageRequestDto.getAuthorName() + "%");
        }

        int count = namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
        log.info("데이터 전체 개수:" + count);
        return count;
    }

}
