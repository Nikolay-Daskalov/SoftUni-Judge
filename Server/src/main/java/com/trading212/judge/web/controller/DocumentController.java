package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.DocumentBindingModel;
import com.trading212.judge.model.dto.DocumentDTO;
import com.trading212.judge.model.dto.DocumentSimpleDTO;
import com.trading212.judge.model.dto.TaskSimpleDTO;
import com.trading212.judge.service.task.DocumentService;
import com.trading212.judge.util.path.ResourcePathUtil;
import com.trading212.judge.web.exception.ServiceUnavailableException;
import com.trading212.judge.web.exception.UnexpectedFailureException;
import com.trading212.judge.web.exception.task.DocumentDataException;
import com.trading212.judge.service.task.TaskService;
import com.trading212.judge.web.exception.task.DocumentExistException;
import com.trading212.judge.web.exception.task.DocumentNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<Set<DocumentSimpleDTO>> getAll() {
        Set<DocumentSimpleDTO> documents = documentService.getAll();

        return !documents.isEmpty() ? ResponseEntity.ok(documents) : ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDTO> create(@ModelAttribute @Valid DocumentBindingModel documentBindingModel,
                                              BindingResult bindingResult, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            throw new DocumentDataException("Invalid data!");
        }

        boolean exist = documentService.isExist(documentBindingModel.name());

        if (exist) {
            throw new DocumentExistException("Document already exists");
        }

        File tempDocFile = null;

        try {
            tempDocFile = File.createTempFile("document", "docx");
            documentBindingModel.document().transferTo(tempDocFile);
        } catch (IOException e) {
            throw new UnexpectedFailureException("Temp file creation exception");
        }

        Optional<DocumentDTO> document = documentService.create(documentBindingModel.name(), documentBindingModel.isTest(),
                documentBindingModel.difficulty(), tempDocFile);

        tempDocFile.delete();

        if (document.isEmpty()) {
            throw new ServiceUnavailableException("Service is shut down temporarily.");
        }

        return ResponseEntity.created(resourcePathUtil.buildResourcePath(httpServletRequest, document.get().id()))
                .body(document.get());
    }

    @DeleteMapping(path = Routes.BY_ID)
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        boolean exist = documentService.isExist(id);

        if (!exist) {
            throw new DocumentNotFoundException("Document does not exist");
        }

        boolean isDeleted = documentService.delete(id);

        if (!isDeleted) {
            throw new ServiceUnavailableException("Document cannot be deleted!");
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = Routes.BY_ID)
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<DocumentDTO> documentDTO = documentService.findByID(id);

        if (documentDTO.isEmpty()) {
            throw new DocumentNotFoundException("Document could not be found!");
        }

        return ResponseEntity.ok(documentDTO);
    }

    @GetMapping(path = Routes.FIND_ALL_TASKS_BY_DOCUMENT_ID)
    public ResponseEntity<Set<TaskSimpleDTO>> findAllTasksSimple(@PathVariable Integer id) {
        if (id <= 0) {
            throw new DocumentDataException("Invalid data!");
        }

        Set<TaskSimpleDTO> allByDocumentId = taskService.findAllByDocument(id);

        return allByDocumentId.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allByDocumentId);
    }

    public static class Routes {
        public static final String BASE = "/api/documents";

        private static final String FIND_ALL_TASKS_BY_DOCUMENT_ID = "/{id}/tasks";

        private static final String BY_ID = "/{id}";
    }
}
