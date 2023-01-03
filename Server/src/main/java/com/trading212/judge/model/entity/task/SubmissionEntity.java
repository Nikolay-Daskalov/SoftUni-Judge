package com.trading212.judge.model.entity.task;

import com.trading212.judge.model.entity.task.enums.SubmissionResultEnum;
import com.trading212.judge.model.entity.user.UserEntity;
import com.trading212.judge.model.entity.base.BaseEntity;

import java.time.Instant;
import java.util.Objects;

public class SubmissionEntity extends BaseEntity {

    public static final String TABLE_NAME = "submissions";

    private TaskEntity task;
    private UserEntity user;
    private SubmissionResultEnum result;
    private LanguageEntity category;
    private Double executionTime;

    public SubmissionEntity() {
        super(null, null);
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

    public LanguageEntity getCategory() {
        return category;
    }

    public Double getExecutionTime() {
        return executionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionEntity that = (SubmissionEntity) o;
        return id.equals(that.id) && task.equals(that.task) && user.equals(that.user) && result == that.result && category.equals(that.category) && Objects.equals(executionTime, that.executionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, user, result, category, executionTime);
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

        public Builder setCategory(LanguageEntity category) {
            submission.category = category;
            return this;
        }

        public Builder setExecutionTime(Double executionTime) {
            submission.executionTime = executionTime;
            return this;
        }

        public Builder setCreatedAt(Instant createdAt) {
            submission.createdAt = createdAt;
            return this;
        }

        public SubmissionEntity build() {
            return submission;
        }
    }
}