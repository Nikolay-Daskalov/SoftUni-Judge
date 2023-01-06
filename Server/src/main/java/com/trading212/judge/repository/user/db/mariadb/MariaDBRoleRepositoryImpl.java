package com.trading212.judge.repository.user.db.mariadb;

import com.trading212.judge.model.entity.user.RoleEntity;
import com.trading212.judge.model.entity.user.enums.RoleEnum;
import com.trading212.judge.repository.user.RoleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MariaDBRoleRepositoryImpl implements RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public MariaDBRoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Integer getStandard() {
        return jdbcTemplate.queryForObject(Queries.GET_STANDARD_ROLE_ID, (rs, rowNum) -> rs.getInt(1));
    }


    private static class Queries {
        private static final String GET_STANDARD_ROLE_ID = String.format("""
                SELECT `id`
                FROM `%s`
                WHERE `role` = '%s'
                """, RoleEntity.TABLE_NAME, RoleEnum.USER.name());
    }
}
