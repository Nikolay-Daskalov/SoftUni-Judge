package com.trading212.judge.model.entity.user;

import com.trading212.judge.model.entity.base.BaseEntity;
import com.trading212.judge.model.entity.user.enums.RoleEnum;

import java.time.Instant;
import java.util.Objects;

public class RoleEntity extends BaseEntity {

    public static final String TABLE_NAME = "roles";

    private final RoleEnum role;

    public RoleEntity(Integer id, RoleEnum role, Instant createdAt) {
        super(id, createdAt);
        this.role = role;
    }

    public RoleEnum getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return id.equals(that.id) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }
}
