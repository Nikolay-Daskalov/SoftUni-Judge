package com.trading212.judge.Server.model.entity.task;

import com.trading212.judge.Server.model.entity.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class DescriptionEntity extends BaseEntity {

    private final String url;

    public DescriptionEntity(Integer id, LocalDateTime createdAt, String url) {
        super(id, createdAt);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptionEntity that = (DescriptionEntity) o;
        return id.equals(that.id) && url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }
}
