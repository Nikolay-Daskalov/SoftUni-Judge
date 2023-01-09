package com.trading212.judge.model.entity.task;

import com.trading212.judge.model.entity.base.BaseEntity;
import com.trading212.judge.service.enums.DocumentDifficulty;

import java.time.Instant;
import java.util.Objects;

public class DocumentEntity extends BaseEntity {

    public static final String TABLE_NAME = "documents";

    private String name;
    private String url;
    private DocumentDifficulty difficulty;
    private Boolean isTest;

    public DocumentEntity() {
        super(null, null);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public DocumentDifficulty getDifficulty() {
        return difficulty;
    }

    public boolean isTest() {
        return isTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentEntity that = (DocumentEntity) o;
        return name.equals(that.name) && url.equals(that.url) && difficulty == that.difficulty && isTest.equals(that.isTest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, difficulty, isTest);
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

        public Builder setDifficulty(DocumentDifficulty difficulty) {
            document.difficulty = difficulty;
            return this;
        }

        public Builder setIsTest(boolean isTest) {
            document.isTest = isTest;
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
            document.difficulty = null;
            document.isTest = null;
            document.createdAt = null;
        }
    }
}
