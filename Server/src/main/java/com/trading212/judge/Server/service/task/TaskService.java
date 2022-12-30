package com.trading212.judge.Server.service.task;

import com.trading212.judge.Server.model.dto.TaskByDescriptionDTO;
import com.trading212.judge.Server.repository.task.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final String ANSWER_URL_PROVIDER_PREFIX = "https://someprovider.com";

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Set<TaskByDescriptionDTO> findAllByDocument(Integer id) {
        return taskRepository.findAllByDescription(id).stream()
                .map(taskEntity -> {
                    Integer taskEntityId = taskEntity.getId();
                    String name = taskEntity.getName();
                    String answersURL = ANSWER_URL_PROVIDER_PREFIX + taskEntity.getAnswersURL();
                    String createdAt = taskEntity.getCreatedAt().toString();

                    return new TaskByDescriptionDTO(taskEntityId, name, answersURL, createdAt);
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
