package com.trading212.judge.repository.task.db.mariadb;

import com.trading212.judge.model.dto.DocumentSimpleDTO;
import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.repository.task.DocumentRepository;
import com.trading212.judge.service.enums.DocumentDifficulty;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MariaDBDocumentRepositoryImpl implements DocumentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public MariaDBDocumentRepositoryImpl(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
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
        AtomicInteger atomicInteger = new AtomicInteger();

        jdbcTemplate.query(Queries.FIND_BY_NAME_RETURN_ID, (rs) -> {
            if (rs.getRow() != 0) {
                atomicInteger.set(rs.getInt(1));
            }
        }, name);

        return atomicInteger.get() != 0 ? Optional.of(atomicInteger.get()) : Optional.empty();
    }

    @Override
    public boolean save(String name, String docURL, DocumentDifficulty difficulty, boolean isTest) {
        try {
            jdbcTemplate.update(Queries.SAVE_ENTITY, name, docURL, difficulty.name(), isTest);
            return true;
        } catch (DataAccessException ignored) {
            return false;
        }
    }

    private static class Queries {
        private static final String GET_ALL = String.format("""
                SELECT d.`id`, d.`name`, d.`difficulty`
                FROM `%s`AS d
                """, DocumentEntity.TABLE_NAME);

        private static final String FIND_BY_NAME_RETURN_ID = String.format("""
                SELECT `id`
                FROM `%s`
                WHERE `name` = ?
                """, DocumentEntity.TABLE_NAME);

        private static final String SAVE_ENTITY = String.format("""
                INSERT INTO `%s` (`name`, `url`, `difficulty`, `is_test`)
                VALUE
                (?, ?, ?, ?)""", DocumentEntity.TABLE_NAME);
    }
}
