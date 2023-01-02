package com.trading212.judge.Server.model.entity.task;

import com.trading212.judge.Server.model.entity.base.BaseEntity;

import java.time.Instant;
import java.util.Objects;

public class DocumentEntity extends BaseEntity {

    public static final String TABLE_NAME = "documents";

    private String name;
    private String url;

    public DocumentEntity() {
        super(null, null);
    }

    public String getName() {
        return name;
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
        private final DocumentEntity document;

        public Builder() {
            document = new DocumentEntity();
        }

        public Builder setId(Integer id) {
            document.id = id;
            return this;
        }

        public Builder setName(String name) {
            document.name = name;
            return this;
        }

        public Builder setURL(String url) {
            document.url = url;
            return this;
        }

        public Builder setCreatedAt(Instant createdAt) {
            document.createdAt = createdAt;
            return this;
        }

        public DocumentEntity build() {
            return document;
        }

        public void reset() {
            document.id = null;
            document.name = null;
            document.url = null;
            document.createdAt = null;
        }
    }
}
