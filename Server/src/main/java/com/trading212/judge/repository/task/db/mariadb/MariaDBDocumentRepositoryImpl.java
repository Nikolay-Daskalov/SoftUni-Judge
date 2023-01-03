package com.trading212.judge.repository.task.db.mariadb;

import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.repository.task.DocumentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public class MariaDBDocumentRepositoryImpl implements DocumentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public MariaDBDocumentRepositoryImpl(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }


    @Override
    public Set<DocumentEntity> getAll() {
        Set<DocumentEntity> documentEntities = new LinkedHashSet<>();

        DocumentEntity.Builder builder = new DocumentEntity.Builder();

        jdbcTemplate.query(Queries.GET_ALL, rs -> {
            int id = rs.getInt(1);
            String url = rs.getString(2);
            Instant createdAt = rs.getTimestamp(3).toInstant();

            builder
                    .setId(id)
                    .setURL(url)
                    .setCreatedAt(createdAt);

            documentEntities.add(builder.build());

            builder.reset();
        });

        return documentEntities;
    }

    private static class Queries {
        private static final String GET_ALL = String.format("""
                SELECT d.`id`, d.`url`, d.`created_at`
                FROM `%s`AS d
                """, DocumentEntity.TABLE_NAME);
    }
}
