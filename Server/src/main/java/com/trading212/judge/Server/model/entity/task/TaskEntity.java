package com.trading212.judge.Server.model.entity.task;

import com.trading212.judge.Server.model.entity.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class TaskEntity extends BaseEntity {

    private String name;
    private DescriptionEntity description;
    private String answerURL;

    public TaskEntity() {
        super(null, null);
    }

    public String getName() {
        return name;
    }

    public String getAnswerURL() {
        return answerURL;
    }

    public DescriptionEntity getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return id.equals(that.id) && name.equals(that.name) && description.equals(that.description) && answerURL.equals(that.answerURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, answerURL);
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

        public Builder setDescription(DescriptionEntity description) {
            task.description = description;
            return this;
        }

        public Builder setAnswerURL(String answerURL) {
            task.answerURL = answerURL;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            task.createdAt = createdAt;
            return this;
        }

        public TaskEntity build() {
            return task;
        }
    }
}
