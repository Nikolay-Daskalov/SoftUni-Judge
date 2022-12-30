package com.trading212.judge.Server.repository.task;

import com.trading212.judge.Server.model.entity.task.TaskEntity;

import java.util.Set;

public interface TaskRepository {

    Set<TaskEntity> findAllByDescription(Integer descriptionId);
}
