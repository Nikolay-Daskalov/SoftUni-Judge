package com.trading212.judge.Server.model.entity.task;

import com.trading212.judge.Server.model.entity.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class DocumentEntity extends BaseEntity {

    public static final String TABLE_NAME = "documents";

    private String url;

    public DocumentEntity() {
        super(null, null);
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentEntity that = (DocumentEntity) o;
        return id.equals(that.id) && url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    public static class Builder {
        private final DocumentEntity description;

        public Builder() {
            description = new DocumentEntity();
        }

        public Builder setId(Integer id) {
            description.id = id;
            return this;
        }

        public Builder setURL(String url) {
            description.url = url;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            description.createdAt = createdAt;
            return this;
        }

        public DocumentEntity build() {
            return description;
        }

        public void reset() {
            description.id = null;
            description.url = null;
            description.createdAt = null;
        }
    }
}
