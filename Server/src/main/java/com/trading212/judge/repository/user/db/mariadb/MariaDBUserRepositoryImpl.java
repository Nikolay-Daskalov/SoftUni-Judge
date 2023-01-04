package com.trading212.judge.repository.user.db.mariadb;

import com.trading212.judge.model.dto.UserDTO;
import com.trading212.judge.model.entity.user.UserEntity;
import com.trading212.judge.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public UserDTO register(String username, String email, String password) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    jdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(Queries.REGISTER);

                        ps.setString(1, username);
                        ps.setString(2, email);
                        ps.setString(3, passwordEncoder.encode(password));

                        return ps;
                    }, keyHolder);
                }
            });
        } catch (TransactionException ignored) {
            LOGGER.error("Transaction failed on insert a new user!");
            return null;
        }

        return new UserDTO();
    }

    @Override
    public boolean isExists(String username) {

        AtomicInteger atomicInteger = new AtomicInteger();

        jdbcTemplate.query(Queries.IS_EXIST, rs -> {
            atomicInteger.set(rs.getInt(1));
        }, username);

        return atomicInteger.get() == 1;
    }

    private static class Queries {
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
    }
}
