package com.trading212.judge.model.entity.base;

import java.time.Instant;

public abstract class BaseEntity {
    protected Integer id;
    protected Instant createdAt;

    protected BaseEntity(Integer id, Instant createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
