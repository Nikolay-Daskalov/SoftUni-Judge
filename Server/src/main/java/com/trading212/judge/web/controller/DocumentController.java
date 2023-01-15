package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.DocumentBindingModel;
import com.trading212.judge.model.dto.task.DocumentDTO;
import com.trading212.judge.model.dto.task.DocumentPageable;
import com.trading212.judge.model.dto.task.TaskPageable;
import com.trading212.judge.model.dto.task.TaskSimpleDTO;
import com.trading212.judge.service.task.DocumentService;
import com.trading212.judge.util.path.ResourcePathUtil;
import com.trading212.judge.web.exception.*;
import com.trading212.judge.service.task.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = DocumentController.Routes.BASE)
public class DocumentController {

    private final TaskService taskService;
    private final DocumentService documentService;
    private final ResourcePathUtil resourcePathUtil;

    public DocumentController(TaskService taskService, DocumentService documentService, ResourcePathUtil resourcePathUtil) {
        this.taskService = taskService;
        this.documentService = documentService;
        this.resourcePathUtil = resourcePathUtil;
    }

    @GetMapping
    public ResponseEntity<DocumentPageable> getAllPageable(@RequestParam(defaultValue = "0") Integer pageNumber) {
        DocumentPageable documents = documentService.getAllPageable(pageNumber);

        if (documents == null) {
            throw new InvalidRequestException();
        }

        return !documents.documents().isEmpty() ? ResponseEntity.ok(documents) : ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDTO> create(@ModelAttribute @Valid DocumentBindingModel documentBindingModel,
                                              BindingResult bindingResult, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }

        File tempDocFile = null;
        try {
            tempDocFile = File.createTempFile("document", "docx");
            documentBindingModel.document().transferTo(tempDocFile);
        } catch (IOException e) {
            throw new UnexpectedFailureException();
        }

        boolean docExist = documentService.isExist(documentBindingModel.name());
        if (docExist) {
            throw new ResourceExistException();
        }

        DocumentDTO document = documentService.create(documentBindingModel.name(), documentBindingModel.isTest(),
                documentBindingModel.difficulty(), tempDocFile);

        tempDocFile.delete();
        if (document == null) {
            throw new InvalidRequestException();
        }

        return ResponseEntity.created(resourcePathUtil.buildResourcePath(httpServletRequest, document.id()))
                .body(document);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(path = Routes.BY_ID)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        Boolean isDeleted = documentService.delete(id);

        if (isDeleted == null) {
            throw new ResourceNotFoundException();
        }

        if (!isDeleted) {
            throw new UnexpectedFailureException();
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = Routes.BY_ID)
    public ResponseEntity<DocumentDTO> findById(@PathVariable Integer id) {
        Optional<DocumentDTO> documentDTO = documentService.findByID(id);

        if (documentDTO.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.ok(documentDTO.get());
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = Routes.FIND_ALL_TASKS_BY_DOCUMENT_ID)
    public ResponseEntity<TaskPageable> findAllTasksSimple(@PathVariable Integer id, @RequestParam(defaultValue = "0") Integer pageNumber) {
        if (id <= 0 || pageNumber < 0) {
            throw new InvalidRequestException();
        }

        boolean exist = documentService.isExist(id);

        if (!exist) {
            throw new ResourceNotFoundException();
        }

        TaskPageable tasksByDoc = taskService.findAllByDocumentPageable(id, pageNumber);

        return tasksByDoc.task().isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tasksByDoc);
    }

    public static class Routes {
        public static final String BASE = "/api/documents";

        public static final String FIND_ALL_TASKS_BY_DOCUMENT_ID = "/{id}/tasks";

        public static final String BY_ID = "/{id}";
    }
}
