package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.DocumentBindingModel;
import com.trading212.judge.model.dto.DocumentSimpleDTO;
import com.trading212.judge.model.dto.TaskSimpleDTO;
import com.trading212.judge.service.task.DocumentService;
import com.trading212.judge.web.exception.DocumentIdNotPositiveException;
import com.trading212.judge.service.task.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = DocumentController.Routes.BASE_ROUTE)
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
    public ResponseEntity<?> create(@ModelAttribute @Valid DocumentBindingModel documentBindingModel,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        boolean isCreated = documentService.create(documentBindingModel.name(), documentBindingModel.isTest(),
                documentBindingModel.difficulty());

        //TODO: maybe change it to 201 plus return created resource
        return isCreated ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping(path = Routes.BY_ID)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        documentService.delete(id);
        return null;
    }

    @GetMapping(path = Routes.FIND_ALL_TASKS_BY_DOCUMENT_ID)
    public ResponseEntity<Set<TaskSimpleDTO>> findAllTasksSimple(@PathVariable Integer id) {
        if (id <= 0) {
            throw new DocumentIdNotPositiveException();
        }

        Set<TaskSimpleDTO> allByDocumentId = taskService.findAllByDocument(id);

        return allByDocumentId.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allByDocumentId);
    }


    public static class Routes {
        public static final String BASE_ROUTE = "/api/documents";

        private static final String FIND_ALL_TASKS_BY_DOCUMENT_ID = "/{id}/tasks";

        private static final String BY_ID = "/{id}";
    }
}
