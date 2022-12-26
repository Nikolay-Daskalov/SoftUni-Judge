package com.trading212.judge.Server.model.entity.task;

import com.trading212.judge.Server.model.entity.task.enums.SubmissionResultEnum;
import com.trading212.judge.Server.model.entity.user.UserEntity;

public class SubmissionEntity {

    private Integer id;
    private TaskEntity task;
    private UserEntity user;
    private SubmissionResultEnum result;
    private LanguageCategoryEntity category;
    private Double executionTime;

    public SubmissionEntity() {
    }

    public Integer getId() {
        return id;
    }

    public TaskEntity getTask() {
        return task;
    }

    public UserEntity getUser() {
        return user;
    }

    public SubmissionResultEnum getResult() {
        return result;
    }

    public LanguageCategoryEntity getCategory() {
        return category;
    }

    public Double getExecutionTime() {
        return executionTime;
    }

    public static class Builder {
        private final SubmissionEntity submission;

        public Builder() {
            submission = new SubmissionEntity();
        }

        public Builder setId(Integer id) {
            submission.id = id;
            return this;
        }

        public Builder setExercise(TaskEntity task) {
            submission.task = task;
            return this;
        }

        public Builder setUser(UserEntity user) {
            submission.user = user;
            return this;
        }

        public Builder setResult(SubmissionResultEnum result) {
            submission.result = result;
            return this;
        }

        public Builder setCategory(LanguageCategoryEntity category) {
            submission.category = category;
            return this;
        }

        public Builder setExecutionTime(Double executionTime) {
            submission.executionTime = executionTime;
            return this;
        }

        public SubmissionEntity build() {
            return submission;
        }
    }
}