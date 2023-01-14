package com.trading212.judge.service.task;

import com.amazonaws.services.s3.AmazonS3;
import com.trading212.judge.api.CloudStorageAPI;
import com.trading212.judge.model.dto.task.TaskDTO;
import com.trading212.judge.model.dto.task.TaskSimpleDTO;
import com.trading212.judge.model.entity.task.TaskEntity;
import com.trading212.judge.repository.task.TaskRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CloudStorageAPI cloudStorageAPI;

    public TaskService(TaskRepository taskRepository, CloudStorageAPI cloudStorageAPI) {
        this.taskRepository = taskRepository;
        this.cloudStorageAPI = cloudStorageAPI;
    }

    public Set<TaskSimpleDTO> findAllByDocument(Integer id) {
        return taskRepository.findAllByDocument(id);
    }

    public boolean isExist(String name) {
        return taskRepository.isExist(name);
    }

    public boolean isExist(Integer id) {
        return taskRepository.isExist(id);
    }

    public TaskDTO create(String name, File file, Integer docId) {
        String answersObjectKey = cloudStorageAPI.createAnswers(name, file);

        Integer taskId = taskRepository.save(name, answersObjectKey, docId);

        Optional<TaskEntity> taskById = taskRepository.findById(taskId);

        return mapTask(taskById).get();
    }

    private Optional<TaskDTO> mapTask(Optional<TaskEntity> taskEntity) {
        if (taskEntity.isEmpty()) {
            return Optional.empty();
        }

        TaskEntity task = taskEntity.get();

        TaskDTO taskDTO = new TaskDTO(
                task.getId(),
                task.getName(),
                task.getDocument().getId(),
                task.getAnswersURL(),
                task.getCreatedAt()
        );

        return Optional.of(taskDTO);
    }

    public boolean deleteAllByDocument(Integer id) {
        return taskRepository.deleteAllByDocument(id);
    }

    public Optional<TaskDTO> findById(Integer id) {
        Optional<TaskEntity> taskEntity = taskRepository.findById(id);

        return mapTask(taskEntity);
    }
}
