package com.trading212.judge.service.task;

import com.trading212.judge.api.CloudStorageAPI;
import com.trading212.judge.model.dto.task.TaskDTO;
import com.trading212.judge.model.dto.task.TaskPageable;
import com.trading212.judge.model.entity.task.TaskEntity;
import com.trading212.judge.repository.task.TaskRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CloudStorageAPI cloudStorageAPI;

    public TaskService(TaskRepository taskRepository, CloudStorageAPI cloudStorageAPI) {
        this.taskRepository = taskRepository;
        this.cloudStorageAPI = cloudStorageAPI;
    }

    public TaskPageable findAllByDocumentPageable(Integer id, Integer pageNumber) {
        return taskRepository.findAllByDocumentPageable(id, pageNumber);
    }

    public boolean isExist(String name) {
        return taskRepository.isExist(name);
    }

    public TaskDTO create(String name, File file, Integer docId) {
        String answersObjectKey = cloudStorageAPI.createAnswers(name, file);

        Integer taskId = taskRepository.save(name, answersObjectKey, docId);

        Optional<TaskEntity> taskEntity = taskRepository.findById(taskId);

        return mapTask(taskEntity).get();
    }

    private Optional<TaskDTO> mapTask(Optional<TaskEntity> taskEntity) {
        if (taskEntity.isEmpty()) {
            return Optional.empty();
        }

        TaskEntity task = taskEntity.get();

        TaskDTO taskDTO = new TaskDTO(
                task.getId(),
                task.getName(),
                task.getDocumentId(),
                cloudStorageAPI.getObjectURL(task.getAnswersURL()),
                task.getCreatedAt()
        );

        return Optional.of(taskDTO);
    }

    public boolean deleteAllByDocumentId(Integer id) {
        Set<String> urls = taskRepository.findAllURLByDocumentId(id);

        CompletableFuture<Boolean> areAnswersDeleted = CompletableFuture
                .supplyAsync(() -> cloudStorageAPI.deleteObjects(urls.toArray(String[]::new)));

        boolean areTasksDeleted = taskRepository.deleteAllByDocument(id);

        try {
            areAnswersDeleted.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return areTasksDeleted;
    }

    public Optional<TaskDTO> findById(Integer id) {
        Optional<TaskEntity> taskEntity = taskRepository.findById(id);

        return mapTask(taskEntity);
    }

    public Set<Integer> findAllByDocumentId(Integer id) {
        return taskRepository.findAllByDocumentId(id);
    }
}
