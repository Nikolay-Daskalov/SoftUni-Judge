package com.trading212.judge.repository.task.db.mariadb;

import com.trading212.judge.model.dto.task.TaskPageable;
import com.trading212.judge.model.dto.task.TaskSimpleDTO;
import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.model.entity.task.TaskEntity;
import com.trading212.judge.repository.task.TaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.ZoneOffset;
import java.util.*;

@Repository
public class MariaDBTaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public MariaDBTaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TaskPageable findAllByDocumentPageable(Integer docId, Integer pageNumber) {
        Set<TaskSimpleDTO> taskEntities = new LinkedHashSet<>();

        jdbcTemplate.query(Queries.FIND_ALL_BY_DOCUMENT_ID_SIMPLE, rs -> {
            Integer id = rs.getInt(1);
            String name = rs.getString(2);

            taskEntities.add(new TaskSimpleDTO(id, name));
        }, docId, Queries.PAGE_SIZE * pageNumber);

        Integer rowCount = jdbcTemplate.queryForObject(Queries.DB_ROWS_COUNT, (rs, rowNum) -> rs.getInt(1));
        Integer totalPages = (int) Math.ceil(rowCount / (Queries.PAGE_SIZE * 1.0));

        return new TaskPageable(taskEntities, totalPages);
    }

    @Override
    public Optional<TaskEntity> findById(Integer id) {
        TaskEntity taskEntity = jdbcTemplate.query(Queries.FIND_BY_ID, rs -> {
            if (!rs.next()) {
                return null;
            }

            return new TaskEntity.Builder()
                    .setId(rs.getInt(1))
                    .setName(rs.getString(2))
                    .setDocumentId(rs.getInt(3))
                    .setAnswersURL(rs.getString(4))
                    .setCreatedAt(rs.getTimestamp(5).toLocalDateTime().toInstant(ZoneOffset.UTC))
                    .build();
        }, id);

        return Optional.ofNullable(taskEntity);
    }

    @Override
    public boolean isExist(String name) {
        Integer result = jdbcTemplate.queryForObject(Queries.IS_EXIST_BY_NAME, (rs, rowNum) -> rs.getInt(1), name);

        return result == 1;
    }

    @Override
    public boolean isExist(Integer id) {
        Integer result = jdbcTemplate.queryForObject(Queries.IS_EXIST_BY_ID, (rs, rowNum) -> rs.getInt(1), id);

        return result == 1;
    }

    @Override
    public Integer save(String name, String answersURL, Integer docId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(Queries.CREATE, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            ps.setString(2, answersURL);
            ps.setInt(3, docId);

            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public boolean deleteAllByDocument(Integer id) {
        jdbcTemplate.update(Queries.DELETE_BY_DOCUMENT_ID, id);
        return true;
    }

    @Override
    public Set<Integer> findAllByDocumentId(Integer id) {
        List<Integer> taskIds = jdbcTemplate.query(Queries.FIND_ALL_ID_BY_DOCUMENT_ID, (rs, rowNum) -> rs.getInt(1), id);

        return new HashSet<>(taskIds);
    }

    @Override
    public Set<String> findAllURLByDocumentId(Integer id) {
        List<String> urls = jdbcTemplate.query(Queries.FIND_ALL_URLS_BY_DOCUMENT_ID, (rs, rowNum) -> rs.getString(1), id);

        return new HashSet<>(urls);
    }


    private static class Queries {

        private static final Integer PAGE_SIZE = 8;
        private static final String FIND_ALL_BY_DOCUMENT_ID_SIMPLE = String.format("""
                SELECT t.`id`, t.`name`
                FROM `%s` AS t
                JOIN `%s` AS d
                ON d.`id` = t.`document_id`
                WHERE d.`id` = ?
                ORDER BY t.`id` ASC
                LIMIT %d OFFSET ?
                """, TaskEntity.TABLE_NAME, DocumentEntity.TABLE_NAME, PAGE_SIZE);
        private static final String DB_ROWS_COUNT = String.format("""
                SELECT COUNT(`id`)
                FROM `%s`
                """, TaskEntity.TABLE_NAME);

        private static final String IS_EXIST_BY_ID = String.format("""
                SELECT COUNT(*)
                FROM `%s`
                WHERE `id` = ?
                """, TaskEntity.TABLE_NAME);

        private static final String IS_EXIST_BY_NAME = String.format("""
                SELECT COUNT(*)
                FROM `%s`
                WHERE `name` = ?
                """, TaskEntity.TABLE_NAME);

        private static final String CREATE = String.format("""
                INSERT INTO `%s` (`name`, `answer_url`, `document_id`)
                VALUE
                (?, ?, ?);
                """, TaskEntity.TABLE_NAME);

        private static final String DELETE_BY_DOCUMENT_ID = String.format("""
                DELETE FROM `%s`
                WHERE `document_id` = ?
                """, TaskEntity.TABLE_NAME);

        private static final String FIND_BY_ID = String.format("""
                SELECT `id`, `name`, `document_id`, `answer_url`, `created_at`
                FROM `%s`
                WHERE `id` = ?
                """, TaskEntity.TABLE_NAME);

        private static final String FIND_ALL_ID_BY_DOCUMENT_ID = String.format("""
                SELECT `id`
                FROM `%s`
                WHERE `document_id` = ?
                """, TaskEntity.TABLE_NAME);

        private static final String FIND_ALL_URLS_BY_DOCUMENT_ID = String.format("""
                SELECT `answer_url`
                FROM `%s`
                WHERE `document_id` = ?
                """, TaskEntity.TABLE_NAME);
    }
}
