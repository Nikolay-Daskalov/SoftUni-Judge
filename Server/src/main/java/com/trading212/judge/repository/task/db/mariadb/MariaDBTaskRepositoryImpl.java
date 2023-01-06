package com.trading212.judge.repository.task.db.mariadb;

import com.trading212.judge.model.dto.TaskSimpleDTO;
import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.model.entity.task.TaskEntity;
import com.trading212.judge.repository.task.TaskRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public class MariaDBTaskRepositoryImpl implements TaskRepository {

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
    public boolean isExist(String name) {
        Integer existCount = jdbcTemplate.queryForObject(Queries.IS_EXIST, (rs, rowNum) -> rs.getInt(1), name);

        return existCount == 1;
    }

    @Override
    public boolean create(String name, String answersURL, Integer docId) {
        try {
            jdbcTemplate.update(Queries.CREATE, name, answersURL, docId);
            return true;
        } catch (DataAccessException ignored) {
            return false;
        }
    }

    @Override
    public boolean deleteByDocument(Integer id) {
        transactionTemplate.getTransactionManager();
        return false;
    }


    private static class Queries {
        private static final String FIND_ALL_BY_DOCUMENT_ID_SIMPLE = String.format("""
                SELECT t.`id`, t.`name`
                FROM `%s` AS t
                JOIN `%s` AS d
                ON d.`id` = t.`document_id`
                WHERE d.`id` = ?
                """, TaskEntity.TABLE_NAME, DocumentEntity.TABLE_NAME);

        private static final String IS_EXIST = String.format("""
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
    }
}
