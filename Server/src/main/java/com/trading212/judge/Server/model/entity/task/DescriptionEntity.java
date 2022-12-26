package com.trading212.judge.Server.model.entity.task;

import java.time.LocalDateTime;

public class DescriptionEntity {

    private Integer id;
    private String url;
    private LocalDateTime createdAt;

    public DescriptionEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
