package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.TaskBindingModel;
import com.trading212.judge.model.dto.task.TaskDTO;
import com.trading212.judge.service.task.DocumentService;
import com.trading212.judge.service.task.TaskService;
import com.trading212.judge.util.path.ResourcePathUtil;
import com.trading212.judge.web.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(path = TaskController.Routes.BASE)
public class TaskController {

    private final TaskService taskService;
    private final DocumentService documentService;
    private final ResourcePathUtil resourcePathUtil;

    public TaskController(TaskService taskService, DocumentService documentService, ResourcePathUtil resourcePathUtil) {
        this.taskService = taskService;
        this.documentService = documentService;
        this.resourcePathUtil = resourcePathUtil;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TaskDTO> create(@ModelAttribute @Valid TaskBindingModel taskBindingModel,
                                          BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }

        boolean taskExist = taskService.isExist(taskBindingModel.name());

        if (taskExist) {
            throw new ResourceExistException();
        }

        boolean docExist = documentService.isExist(taskBindingModel.documentId());

        if (!docExist) {
            throw new InvalidRequestException();
        }

        File jsonFile = null;

        try {
            jsonFile = File.createTempFile("answers", "json");
            taskBindingModel.answers().transferTo(jsonFile);
        } catch (IOException e) {
            throw new UnexpectedFailureException();
        }

        TaskDTO taskDTO = taskService.create(taskBindingModel.name(), jsonFile, taskBindingModel.documentId());

        jsonFile.delete();

        return ResponseEntity.created(resourcePathUtil.buildResourcePath(httpServletRequest, taskDTO.id()))
                .body(taskDTO);
    }

    @GetMapping(path = Routes.BY_ID)
    public ResponseEntity<TaskDTO> findById(@PathVariable Integer id) {
        Optional<TaskDTO> taskDTO = taskService.findById(id);

        if (taskDTO.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.ok(taskDTO.get());
    }

    public static class Routes {
        public static final String BASE = "/api/tasks";
        public static final String BY_ID = "/{id}";
    }
}
