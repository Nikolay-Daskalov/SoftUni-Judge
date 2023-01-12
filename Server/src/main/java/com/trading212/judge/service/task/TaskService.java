package com.trading212.judge.service.task;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.trading212.judge.model.dto.TaskDTO;
import com.trading212.judge.model.dto.TaskSimpleDTO;
import com.trading212.judge.model.entity.task.TaskEntity;
import com.trading212.judge.repository.task.TaskRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final AmazonS3 amazonS3;

    public TaskService(TaskRepository taskRepository, AmazonS3 amazonS3) {
        this.taskRepository = taskRepository;
        this.amazonS3 = amazonS3;
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

    public Optional<TaskDTO> create(String taskName, File file, Integer docId) {

        amazonS3.putObject("trading212-judge-submissions", taskName + "-answers.json", file);

        String taskAnswersURL = amazonS3.getUrl("trading212-judge-submissions", taskName).toString();

        Optional<Integer> savedTask = taskRepository.save(taskName, taskAnswersURL, docId);

        if (savedTask.isEmpty()) {
            return Optional.empty();
        }

        Optional<TaskEntity> taskById = taskRepository.findById(savedTask.get());

        return mapTask(taskById);
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
