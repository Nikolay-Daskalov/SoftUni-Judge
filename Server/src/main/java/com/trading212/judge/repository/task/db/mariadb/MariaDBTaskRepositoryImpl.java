package com.trading212.judge.repository.task.db.mariadb;

import com.trading212.judge.model.dto.TaskSimpleDTO;
import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.model.entity.task.TaskEntity;
import com.trading212.judge.repository.task.TaskRepository;
import com.trading212.judge.service.enums.DocumentDifficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.ZoneOffset;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class MariaDBTaskRepositoryImpl implements TaskRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(MariaDBTaskRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public MariaDBTaskRepositoryImpl(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public Set<TaskSimpleDTO> findAllByDocument(Integer documentId) {
        Set<TaskSimpleDTO> taskEntities = new LinkedHashSet<>();

        jdbcTemplate.query(Queries.FIND_ALL_BY_DOCUMENT_ID_SIMPLE, rs -> {
            Integer id = rs.getInt(1);
            String name = rs.getString(2);

            taskEntities.add(new TaskSimpleDTO(id, name));
        }, documentId);

        return taskEntities;
    }

    @Override
    public Optional<TaskEntity> findById(Integer id) {
        TaskEntity taskEntity = jdbcTemplate.query(Queries.FIND_BY_ID, rs -> {
            if (!rs.next()) {
                return null;
            }

            DocumentEntity documentEntity = new DocumentEntity.Builder()
                    .setId(rs.getInt(5))
                    .setName(rs.getString(6))
                    .setURL(rs.getString(7))
                    .setDifficulty(DocumentDifficulty.valueOf(rs.getString(8)))
                    .setIsTest(rs.getBoolean(9))
                    .setCreatedAt(rs.getTimestamp(10).toLocalDateTime().toInstant(ZoneOffset.UTC))
                    .build();


            return new TaskEntity.Builder()
                    .setId(rs.getInt(1))
                    .setName(rs.getString(2))
                    .setAnswersURL(rs.getString(3))
                    .setCreatedAt(rs.getTimestamp(4).toLocalDateTime().toInstant(ZoneOffset.UTC))
                    .setDocument(documentEntity)
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
    public Optional<Integer> save(String name, String answersURL, Integer docId) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(Queries.CREATE, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, name);
                ps.setString(2, answersURL);
                ps.setInt(3, docId);

                return ps;
            }, keyHolder);

            return Optional.of(keyHolder.getKey().intValue());
        } catch (DataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteAllByDocument(Integer id) {
        jdbcTemplate.update(Queries.DELETE_BY_DOCUMENT_ID, id);
        return true;
    }


    private static class Queries {
        private static final String FIND_ALL_BY_DOCUMENT_ID_SIMPLE = String.format("""
                SELECT t.`id`, t.`name`
                FROM `%s` AS t
                JOIN `%s` AS d
                ON d.`id` = t.`document_id`
                WHERE d.`id` = ?
                """, TaskEntity.TABLE_NAME, DocumentEntity.TABLE_NAME);

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
                SELECT t.`id`, t.`name`, t.`answer_url`, t.`created_at`,
                d.`id`, d.`name`, d.`url`, d.`difficulty`, d.`is_test`, d.`created_at`
                FROM `%s` AS t
                JOIN `%s` AS d
                ON t.`document_id` = d.`id`
                WHERE t.`id` = ?
                """, TaskEntity.TABLE_NAME, DocumentEntity.TABLE_NAME);
    }
}
