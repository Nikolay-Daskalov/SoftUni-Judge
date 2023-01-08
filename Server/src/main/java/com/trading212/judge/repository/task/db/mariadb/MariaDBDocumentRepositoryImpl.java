package com.trading212.judge.repository.task.db.mariadb;

import com.trading212.judge.model.dto.DocumentDTO;
import com.trading212.judge.model.dto.DocumentSimpleDTO;
import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.model.entity.task.TaskEntity;
import com.trading212.judge.repository.task.DocumentRepository;
import com.trading212.judge.service.enums.DocumentDifficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class MariaDBDocumentRepositoryImpl implements DocumentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MariaDBDocumentRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final MariaDBTaskRepositoryImpl mariaDBTaskRepository;

    public MariaDBDocumentRepositoryImpl(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate, MariaDBTaskRepositoryImpl mariaDBTaskRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        this.mariaDBTaskRepository = mariaDBTaskRepository;
    }


    @Override
    public Set<DocumentSimpleDTO> getAll() {
        Set<DocumentSimpleDTO> documents = new LinkedHashSet<>();

        jdbcTemplate.query(Queries.GET_ALL, rs -> {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            DocumentDifficulty difficulty = DocumentDifficulty.valueOf(rs.getString(3));

            documents.add(new DocumentSimpleDTO(id, name, difficulty));
        });

        return documents;
    }

    @Override
    public Optional<Integer> findByName(String name) {
        Integer id = jdbcTemplate.queryForObject(Queries.FIND_BY_NAME, (rs, rowNum) -> rs.getInt(1), name);

        return Optional.of(id);//TODO: use different method
    }

    @Override
    public Optional<DocumentEntity> findById(Integer id) {
        DocumentEntity documentEntity = jdbcTemplate.query(Queries.FIND_BY_ID, (rs) -> {
            if (!rs.next()) {
                return null;
            }

            return new DocumentEntity.Builder()
                    .setId(rs.getInt(1))
                    .setName(rs.getString(2))
                    .setURL(rs.getString(3))
                    .setDifficulty(DocumentDifficulty.valueOf(rs.getString(4)))
                    .setIsTest(rs.getBoolean(5))
                    .setCreatedAt(rs.getTimestamp(6).toLocalDateTime().toInstant(ZoneOffset.UTC))
                    .build();
        }, id);

        return Optional.ofNullable(documentEntity);
    }

    @Override
    public Optional<Integer> save(String name, String docURL, DocumentDifficulty difficulty, boolean isTest) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(Queries.SAVE_ENTITY, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, name);
                ps.setString(2, docURL);
                ps.setString(3, difficulty.name());
                ps.setBoolean(4, isTest);

                return ps;
            }, keyHolder);

            return Optional.of(keyHolder.getKey().intValue());
        } catch (DataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isExist(String name) {
        Integer result = jdbcTemplate.queryForObject(Queries.IS_EXIST, (rs, rowNum) -> rs.getInt(1), name);

        return result == 1;
    }

    @Override
    public boolean delete(Integer id) {
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    boolean isDeleted = mariaDBTaskRepository.deleteByDocument(id);

                    if (!isDeleted) {
                        status.setRollbackOnly();
                        return;
                    }

                    jdbcTemplate.update(Queries.DELETE_BY_ID, id);
                }
            });
            return true;
        } catch (TransactionException ignored) {
            LOGGER.error("Deleting a document failed");
            return false;
        }
    }

    private static class Queries {
        private static final String GET_ALL = String.format("""
                SELECT d.`id`, d.`name`, d.`difficulty`
                FROM `%s`AS d
                """, DocumentEntity.TABLE_NAME);

        private static final String FIND_BY_NAME = String.format("""
                SELECT `id`
                FROM `%s`
                WHERE `name` = ?
                """, DocumentEntity.TABLE_NAME);

        private static final String FIND_BY_ID = String.format("""
                SELECT `id`, `name`, `url`, `difficulty`, `is_test`, `created_at`
                FROM `%s`
                WHERE `id` = ?
                """, DocumentEntity.TABLE_NAME);

        private static final String SAVE_ENTITY = String.format("""
                INSERT INTO `%s` (`name`, `url`, `difficulty`, `is_test`)
                VALUE
                (?, ?, ?, ?)
                """, DocumentEntity.TABLE_NAME);

        private static final String IS_EXIST = String.format("""
                SELECT COUNT(`id`)
                FROM `%s`
                WHERE `name` = ?
                """, DocumentEntity.TABLE_NAME);

        private static final String DELETE_BY_ID = String.format("""
                DELETE FROM `%s`
                WHERE `id` = ?
                """, DocumentEntity.TABLE_NAME);
    }
}
