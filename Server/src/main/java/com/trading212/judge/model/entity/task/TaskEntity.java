package com.trading212.judge.model.entity.task;

import com.trading212.judge.model.entity.base.BaseEntity;

import java.time.Instant;
import java.util.Objects;

public class TaskEntity extends BaseEntity {

    public static final String TABLE_NAME = "tasks";

    private String name;
    private DocumentEntity description;
    private String answersURL;

    public TaskEntity() {
        super(null, null);
    }

    public String getName() {
        return name;
    }

    public String getAnswersURL() {
        return answersURL;
    }

    public DocumentEntity getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return id.equals(that.id) && name.equals(that.name) && description.equals(that.description) && answersURL.equals(that.answersURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, answersURL);
    }

    public static class Builder {
        private final TaskEntity task;

        public Builder() {
            task = new TaskEntity();
        }

        public Builder setId(Integer id) {
            task.id = id;
            return this;
        }

        public Builder setName(String name) {
            task.name = name;
            return this;
        }

        public Builder setDescription(DocumentEntity description) {
            task.description = description;
            return this;
        }

        public Builder setAnswersURL(String answersURL) {
            task.answersURL = answersURL;
            return this;
        }

        public Builder setCreatedAt(Instant createdAt) {
            task.createdAt = createdAt;
            return this;
        }

        public TaskEntity build() {
            return task;
        }

        public void reset() {
            task.id = null;
            task.name = null;
            task.description = null;
            task.answersURL = null;
            task.createdAt = null;
        }
    }
}
