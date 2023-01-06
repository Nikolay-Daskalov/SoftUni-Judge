package com.trading212.judge.repository.task;

import com.trading212.judge.model.dto.TaskSimpleDTO;
import com.trading212.judge.model.entity.task.TaskEntity;

import java.util.Set;

public interface TaskRepository {

    Set<TaskSimpleDTO> findAllByDocument(Integer descriptionId);

    boolean isExist(String name);

    boolean create(String name, String answersURL, Integer docId);

    boolean deleteByDocument(Integer id);
}
