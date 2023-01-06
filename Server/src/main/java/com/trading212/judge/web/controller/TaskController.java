package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.TaskBindingModel;
import com.trading212.judge.model.dto.TaskCreationDTO;
import com.trading212.judge.service.task.TaskService;
import com.trading212.judge.web.exception.TaskCreationException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(path = TaskController.Route.BASE)
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> findByDescription(@ModelAttribute @Valid TaskBindingModel taskBindingModel,
                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new TaskCreationException("Task fields not valid!");
        }

        File jsonFile = null;

        try {
            jsonFile = File.createTempFile("answers", "json");
            taskBindingModel.answers().transferTo(jsonFile);
        } catch (IOException e) {
            LOGGER.error("Temp File creation failed");
            LOGGER.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        TaskCreationDTO task = new TaskCreationDTO(
                taskBindingModel.name(),
                taskBindingModel.documentName(),
                jsonFile
        );

        boolean isCreated = taskService.create(task);

        return isCreated ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    public static class Route {
        public static final String BASE = "/api/tasks";
    }
}
