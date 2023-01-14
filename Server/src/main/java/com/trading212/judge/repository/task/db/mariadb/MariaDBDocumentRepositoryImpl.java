package com.trading212.judge.repository.task.db.mariadb;

import com.trading212.judge.model.dto.task.DocumentPageable;
import com.trading212.judge.model.dto.task.DocumentSimpleDTO;
import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.repository.task.DocumentRepository;
import com.trading212.judge.service.enums.DocumentDifficulty;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneOffset;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class MariaDBDocumentRepositoryImpl implements DocumentRepository {

    private final JdbcTemplate jdbcTemplate;

    public MariaDBDocumentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public DocumentPageable getAllPageable(Integer pageNumber) {
        Set<DocumentSimpleDTO> documents = new LinkedHashSet<>();

        jdbcTemplate.query(Queries.GET_ALL_PAGEABLE, rs -> {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            DocumentDifficulty difficulty = DocumentDifficulty.valueOf(rs.getString(3));
            boolean isTest = rs.getBoolean(4);

            documents.add(new DocumentSimpleDTO(id, name, difficulty, isTest));
        }, Queries.PAGE_SIZE * pageNumber);

        Integer rowCount = jdbcTemplate.queryForObject(Queries.DB_ROWS_COUNT, (rs, rowNum) -> rs.getInt(1));
        Integer totalPages = (int) Math.ceil(rowCount / (Queries.PAGE_SIZE * 1.0));

        return new DocumentPageable(documents, totalPages);
    }

    @Override
    public Optional<DocumentEntity> findByName(String name) {
        DocumentEntity documentEntity = jdbcTemplate.query(Queries.FIND_BY_NAME, rs -> {
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
        }, name);

        return Optional.ofNullable(documentEntity);
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
    public Integer save(String name, String docURL, DocumentDifficulty difficulty, boolean isTest) {
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

            return keyHolder.getKey().intValue();
        } catch (DataAccessException ignored) {
            return null;
        }
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
    public boolean delete(Integer id) {
        jdbcTemplate.update(Queries.DELETE_BY_ID, id);
        return true;
    }

    @Override
    public String findURLById(Integer id) {
        return jdbcTemplate.queryForObject(Queries.FIND_URL_BY_ID, (rs, rowNum) -> rs.getString(1), id);
    }

    private static class Queries {

        private static final Integer PAGE_SIZE = 8;
        private static final String GET_ALL_PAGEABLE = String.format("""
                SELECT `id`, `name`, `difficulty`, `is_test`
                FROM `%s`
                ORDER BY `id` ASC
                LIMIT %d OFFSET ?
                """, DocumentEntity.TABLE_NAME, PAGE_SIZE);

        private static final String DB_ROWS_COUNT = String.format("""
                SELECT COUNT(`id`)
                FROM `%s`
                """, DocumentEntity.TABLE_NAME);

        private static final String FIND_BY_NAME = String.format("""
                SELECT `id`, `name`, `url`, `difficulty`, `is_test`, `created_at`
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

        private static final String IS_EXIST_BY_NAME = String.format("""
                SELECT COUNT(`id`)
                FROM `%s`
                WHERE `name` = ?
                """, DocumentEntity.TABLE_NAME);

        private static final String IS_EXIST_BY_ID = String.format("""
                SELECT COUNT(`id`)
                FROM `%s`
                WHERE `id` = ?
                """, DocumentEntity.TABLE_NAME);

        private static final String DELETE_BY_ID = String.format("""
                DELETE FROM `%s`
                WHERE `id` = ?
                """, DocumentEntity.TABLE_NAME);

        private static final String FIND_URL_BY_ID = String.format("""
                SELECT `url`
                FROM `%s`
                WHERE `id` = ?
                """, DocumentEntity.TABLE_NAME);
    }
}
