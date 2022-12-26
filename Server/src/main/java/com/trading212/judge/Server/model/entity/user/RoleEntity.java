package com.trading212.judge.Server.model.entity.user;

import com.trading212.judge.Server.model.entity.user.enums.RoleEnum;

public class RoleEntity {

    private final Integer id;
    private final RoleEnum role;

    public RoleEntity(Integer id, String role) {
        this.id = id;
        this.role = RoleEnum.valueOf(role);
    }

    public Integer getId() {
        return id;
    }

    public RoleEnum getRole() {
        return role;
    }
}
