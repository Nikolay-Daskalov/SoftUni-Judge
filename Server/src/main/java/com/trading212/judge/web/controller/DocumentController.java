package com.trading212.judge.web.controller;

import com.trading212.judge.model.dto.TaskByDescriptionDTO;
import com.trading212.judge.web.exception.DocumentIdNotPositiveException;
import com.trading212.judge.service.task.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = DocumentController.Routes.BASE_ROUTE)
public class DocumentController {

    private final TaskService taskService;

    public DocumentController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public ResponseEntity<Set<?>> getAll() {
        return null;
    }

    @GetMapping(path = Routes.FIND_ALL_TASKS_BY_DOCUMENT_ID)
    public ResponseEntity<Set<TaskByDescriptionDTO>> findAllTasks(@PathVariable Integer id) {
        if (id <= 0) {
            throw new DocumentIdNotPositiveException();
        }

        Set<TaskByDescriptionDTO> allByDescription = taskService.findAllByDocument(id);

        return allByDescription.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allByDescription);
    }


    public static class Routes {
        public static final String BASE_ROUTE = "/api/documents";

        private static final String FIND_ALL_TASKS_BY_DOCUMENT_ID = "/{id}/tasks";
    }
}
