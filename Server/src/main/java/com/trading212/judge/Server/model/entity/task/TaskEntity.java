package com.trading212.judge.Server.model.entity.task;

import java.time.LocalDateTime;

public class TaskEntity {

    private Integer id;
    private String name;
    private DescriptionEntity description;
    private String answerURL;
    private LocalDateTime createdAt;

    public TaskEntity() {
    }

    public Integer getId() {
        return id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
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
