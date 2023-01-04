package com.trading212.judge.repository.user.db.mariadb;

import com.trading212.judge.model.auth.UserAuthModel;
import com.trading212.judge.repository.user.UserAuthenticationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Repository
public class MariaDBUserAuthenticationRepositoryImpl implements UserAuthenticationRepository {

    private final JdbcTemplate jdbcTemplate;

    public MariaDBUserAuthenticationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserAuthModel> findByUsername(String username) {
        UserAuthModel userAuthModel = new UserAuthModel();

        Set<String> roles = new HashSet<>();

        AtomicBoolean isFound = new AtomicBoolean(false);

        jdbcTemplate.query(Queries.FIND_BY_USERNAME, rs -> {
            if (rs.getRow() == 0) {
                return;
            }

            if (rs.isFirst()) {
                isFound.set(true);

                userAuthModel.setUsername(rs.getString(1));
                userAuthModel.setPasswordHash(rs.getString(2));

                roles.add(rs.getString(3));
            } else {
                roles.add(rs.getString(3));
            }
        }, username);

        userAuthModel.setRoles(roles);

        return isFound.get() ? Optional.of(userAuthModel) : Optional.empty();
    }

    private static class Queries {
        private static final String FIND_BY_USERNAME = """
                SELECT u.`username`, u.`password_hash`, r.`role`
                FROM `users` AS u
                JOIN `users_roles` AS ur
                ON ur.`user_id` = u.`id`
                JOIN `roles` AS r
                ON ur.`role_id` = r.`id`
                WHERE u.`username` = ?
                """;
    }
}
