package com.trading212.judge.Server.model.entity.user;

import com.trading212.judge.Server.model.entity.base.BaseEntity;
import com.trading212.judge.Server.model.entity.user.enums.RoleEnum;

import java.time.LocalDateTime;

public class RoleEntity extends BaseEntity {

    private final RoleEnum role;

    public RoleEntity(Integer id, RoleEnum role, LocalDateTime createdAt) {
        super(id, createdAt);
        this.role = role;
    }

    public RoleEnum getRole() {
        return role;
    }
}
