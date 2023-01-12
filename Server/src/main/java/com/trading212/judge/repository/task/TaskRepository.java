package com.trading212.judge.repository.task;

import com.trading212.judge.model.dto.TaskSimpleDTO;
import com.trading212.judge.model.entity.task.TaskEntity;

import java.util.Optional;
import java.util.Set;

public interface TaskRepository {

    Set<TaskSimpleDTO> findAllByDocument(Integer descriptionId);

    Optional<TaskEntity> findById(Integer id);

    boolean isExist(String name);

    boolean isExist(Integer id);

    Optional<Integer> save(String name, String answersURL, Integer docId);

    boolean deleteAllByDocument(Integer id);
}
