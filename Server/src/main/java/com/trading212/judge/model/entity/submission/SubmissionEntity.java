package com.trading212.judge.model.entity.submission;

import com.trading212.judge.model.entity.base.BaseEntity;
import com.trading212.judge.service.enums.CodeResultEnum;

import java.time.Instant;
import java.util.Objects;

public class SubmissionEntity extends BaseEntity {

    public static final String TABLE_NAME = "submissions";

    private Integer taskId;
    private Integer userId;
    private CodeResultEnum result;
    private Integer codeLanguageId;
    private String executionTime;

    public SubmissionEntity() {
        super(null, null);
    }

    public Integer getTaskId() {
        return taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public CodeResultEnum getResult() {
        return result;
    }

    public Integer getCodeLanguageId() {
        return codeLanguageId;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionEntity that = (SubmissionEntity) o;
        return id.equals(that.id) && taskId.equals(that.taskId) && userId.equals(that.userId) && result == that.result && codeLanguageId.equals(that.codeLanguageId) && executionTime.equals(that.executionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskId, userId, result, codeLanguageId, executionTime);
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

        public Builder setTaskId(Integer taskId) {
            submission.taskId = taskId;
            return this;
        }

        public Builder setUserId(Integer userId) {
            submission.userId = userId;
            return this;
        }

        public Builder setResult(CodeResultEnum result) {
            submission.result = result;
            return this;
        }

        public Builder setCodeLanguageId(Integer codeLanguageId) {
            submission.codeLanguageId = codeLanguageId;
            return this;
        }

        public Builder setExecutionTime(String executionTime) {
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