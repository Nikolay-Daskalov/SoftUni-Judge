package com.trading212.judge.repository.user.db.mariadb;

import com.trading212.judge.model.entity.user.UserEntity;
import com.trading212.judge.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


@Repository
public class MariaDBUserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MariaDBUserRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final PasswordEncoder passwordEncoder;

    public MariaDBUserRepositoryImpl(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public boolean register(String username, String email, String password, Integer standardRoleId) {
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    KeyHolder keyHolder = new GeneratedKeyHolder();

                    jdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(Queries.REGISTER, Statement.RETURN_GENERATED_KEYS);

                        ps.setString(1, username);
                        ps.setString(2, email);
                        ps.setString(3, passwordEncoder.encode(password));

                        return ps;
                    }, keyHolder);

                    BigInteger userId = (BigInteger) keyHolder.getKey();

                    jdbcTemplate.update(Queries.SET_STANDARD_ROLE, userId.intValue(), standardRoleId);
                }
            });

            return true;
        } catch (TransactionException | DataAccessException ignored) {
            LOGGER.error("Transaction failed to insert a new user!");
            return false;
        }
    }

    @Override
    public boolean isExists(String username) {
        Integer existCount = jdbcTemplate.queryForObject(Queries.IS_EXIST, (rs, rowNumber) -> rs.getInt(1), username);

        return existCount == 1;
    }

    @Override
    public Optional<Integer> getIdByUsername(String username) {
        return jdbcTemplate.query(Queries.GET_ID_BY_USERNAME, (rs) -> {
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(rs.getInt(1));
        }, username);
    }

    private static class Queries {
        private static final String USERS_ROLES_TABLE_NAME = "users_roles";

        private static final String REGISTER = String.format("""
                INSERT INTO `%s` (`username`, `email`, `password_hash`)
                VALUE
                (?, ?, ?)
                """, UserEntity.TABLE_NAME);

        private static final String IS_EXIST = String.format("""
                SELECT COUNT(`username`)
                FROM `%s`
                WHERE `username` = ?
                """, UserEntity.TABLE_NAME);

        private static final String SET_STANDARD_ROLE = String.format("""
                INSERT INTO `%s` (`user_id`, `role_id`)
                VALUE
                (?, ?)
                """, USERS_ROLES_TABLE_NAME);

        private static final String GET_ID_BY_USERNAME = String.format("""
                SELECT `id`
                FROM `%s`
                WHERE `username` = ?
                """, UserEntity.TABLE_NAME);
    }
}
