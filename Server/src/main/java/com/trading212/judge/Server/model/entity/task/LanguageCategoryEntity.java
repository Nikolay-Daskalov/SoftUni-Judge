package com.trading212.judge.Server.model.entity.task;

import com.trading212.judge.Server.model.entity.task.enums.TaskCategoryEnum;

import java.time.LocalDateTime;

public class LanguageCategoryEntity {

    private Integer id;
    private TaskCategoryEnum taskCategory;
    private LocalDateTime createdAt;

    public LanguageCategoryEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskCategoryEnum getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(TaskCategoryEnum taskCategory) {
        this.taskCategory = taskCategory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
