package com.trading212.judge.Server.model.entity.base;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    protected Integer id;
    protected LocalDateTime createdAt;

    protected BaseEntity(Integer id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
