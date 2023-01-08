package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.DocumentBindingModel;
import com.trading212.judge.model.dto.DocumentDTO;
import com.trading212.judge.model.dto.DocumentSimpleDTO;
import com.trading212.judge.model.dto.TaskSimpleDTO;
import com.trading212.judge.service.task.DocumentService;
import com.trading212.judge.web.exception.ServiceUnavailableException;
import com.trading212.judge.web.exception.task.DocumentDataException;
import com.trading212.judge.service.task.TaskService;
import com.trading212.judge.web.exception.task.DocumentExistException;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = DocumentController.Routes.BASE)
public class DocumentController {

    private final TaskService taskService;
    private final DocumentService documentService;

    public DocumentController(TaskService taskService, DocumentService documentService) {
        this.taskService = taskService;
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<Set<DocumentSimpleDTO>> getAll() {
        Set<DocumentSimpleDTO> documents = documentService.getAll();

        return !documents.isEmpty() ? ResponseEntity.ok(documents) : ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDTO> create(@ModelAttribute @Valid DocumentBindingModel documentBindingModel,
                                              BindingResult bindingResult) {

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
            documentBindingModel.file().transferTo(tempDocFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        Optional<DocumentDTO> document = documentService.create(documentBindingModel.name(), documentBindingModel.isTest(),
                documentBindingModel.difficulty(), tempDocFile);

        tempDocFile.delete();

        if (document.isEmpty()) {
            throw new ServiceUnavailableException("Service is shut down temporarily.");
        }

        return ResponseEntity.created(URI.create("someurl")).body(document.get());
    }

    @DeleteMapping(path = Routes.BY_ID)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        boolean isDeleted = documentService.delete(id);

        if (!isDeleted) {
            throw new DocumentDataException("Data invalid");
        }

        return ResponseEntity.ok().build();
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
