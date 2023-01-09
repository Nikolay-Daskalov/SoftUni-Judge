package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.TaskBindingModel;
import com.trading212.judge.model.dto.DocumentDTO;
import com.trading212.judge.model.dto.TaskDTO;
import com.trading212.judge.service.task.DocumentService;
import com.trading212.judge.service.task.TaskService;
import com.trading212.judge.util.path.ResourcePathUtil;
import com.trading212.judge.web.exception.ServiceUnavailableException;
import com.trading212.judge.web.exception.UnexpectedFailureException;
import com.trading212.judge.web.exception.task.TaskCreationException;
import com.trading212.judge.web.exception.task.TaskExistException;
import com.trading212.judge.web.exception.task.TaskNotFoundException;
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
    public ResponseEntity<TaskDTO> findByDescription(@ModelAttribute @Valid TaskBindingModel taskBindingModel,
                                                     BindingResult bindingResult, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            throw new TaskCreationException("Task fields not valid!");
        }

        boolean taskExist = taskService.isExist(taskBindingModel.name());

        if (taskExist) {
            throw new TaskExistException("Task already exists!");
        }

        Optional<DocumentDTO> docByName = documentService.findByID(taskBindingModel.documentName());

        if (docByName.isEmpty()) {
            throw new TaskCreationException("Document does not exists");
        }

        File jsonFile = null;

        try {
            jsonFile = File.createTempFile("answers", "json");
            taskBindingModel.answers().transferTo(jsonFile);
        } catch (IOException e) {
            throw new UnexpectedFailureException("Temp file creation failed");
        }

        Optional<TaskDTO> taskDTO = taskService.create(taskBindingModel.name(), jsonFile, docByName.get().id());

        jsonFile.delete();

        if (taskDTO.isEmpty()) {
            throw new ServiceUnavailableException("Task cannot be created.");
        }

        return ResponseEntity.created(resourcePathUtil.buildResourcePath(httpServletRequest, taskDTO.get().id()))
                .body(taskDTO.get());
    }

    @GetMapping(path = Routes.BY_ID)
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<TaskDTO> taskDTO = taskService.findById(id);

        if (taskDTO.isEmpty()) {
            throw new TaskNotFoundException("Task could not be found!");
        }

        return ResponseEntity.ok(taskDTO);
    }

    public static class Routes {
        public static final String BASE = "/api/tasks";
        public static final String BY_ID = "/{id}";
    }
}
