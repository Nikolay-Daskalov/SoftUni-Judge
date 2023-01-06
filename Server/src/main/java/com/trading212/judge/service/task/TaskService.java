package com.trading212.judge.service.task;

import com.trading212.judge.model.dto.TaskByDescriptionDTO;
import com.trading212.judge.model.dto.TaskCreationDTO;
import com.trading212.judge.model.dto.TaskSimpleDTO;
import com.trading212.judge.repository.task.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final String ANSWER_URL_PROVIDER_PREFIX = "https://someprovider.com";

    private final TaskRepository taskRepository;
    private final DocumentService documentService;

    public TaskService(TaskRepository taskRepository, DocumentService documentService) {
        this.taskRepository = taskRepository;
        this.documentService = documentService;
    }

    public Set<TaskSimpleDTO> findAllByDocument(Integer id) {
        return taskRepository.findAllByDocument(id);
    }

    private boolean isExist(String name) {
        return taskRepository.isExist(name);
    }

    public boolean create(TaskCreationDTO taskCreationDTO) {
        boolean exist = isExist(taskCreationDTO.name());

        if (exist) {
            return false;
        }

        Optional<Integer> docId = documentService.findByName(taskCreationDTO.documentName());

        if (docId.isEmpty()) {
            return false;
        }

        //TODO: send it to AWS and save url;

        taskCreationDTO.answersJSON().delete();

        return taskRepository.create(taskCreationDTO.name(), "someURL", docId.get());
    }

    public boolean deleteByDocument(Integer id) {
        return taskRepository.deleteByDocument(id);
    }
}
