package com.trading212.judge.repository.task;

import com.trading212.judge.model.entity.task.TaskEntity;

import java.util.Set;

public interface TaskRepository {

    Set<TaskEntity> findAllByDescription(Integer descriptionId);
}
