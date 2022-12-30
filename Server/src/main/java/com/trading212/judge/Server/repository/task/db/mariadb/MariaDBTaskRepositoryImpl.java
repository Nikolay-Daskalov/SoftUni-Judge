package com.trading212.judge.Server.repository.task.db.mariadb;

import com.trading212.judge.Server.model.entity.task.DocumentEntity;
import com.trading212.judge.Server.model.entity.task.TaskEntity;
import com.trading212.judge.Server.repository.task.TaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
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
    public Set<TaskEntity> findAllByDescription(Integer descriptionId) {
        Set<TaskEntity> taskEntities = new LinkedHashSet<>();

        TaskEntity.Builder builder = new TaskEntity.Builder();

        jdbcTemplate.query(Queries.FIND_ALL_BY_DESCRIPTION_ID, rs -> {
            Integer id = rs.getInt(1);
            String name = rs.getString(2);
            String answersURL = rs.getString(3);
            LocalDateTime createdAt = rs.getTimestamp(4).toLocalDateTime();

            builder
                    .setId(id)
                    .setName(name)
                    .setAnswersURL(answersURL)
                    .setCreatedAt(createdAt);

            taskEntities.add(builder.build());

            builder.reset();
        }, descriptionId);

        return taskEntities;
    }


    private static class Queries {
        private final static String FIND_ALL_BY_DESCRIPTION_ID = String.format("""
                SELECT t.`id`, t.`name`, t.`answer_url`, t.`created_at`
                FROM `%s` AS t
                JOIN `%s` AS d
                ON d.`id` = t.`document_id`
                WHERE d.`id` = ?
                """, TaskEntity.TABLE_NAME, DocumentEntity.TABLE_NAME);
    }
}
