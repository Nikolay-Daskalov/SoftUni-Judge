package com.trading212.judge.service.task;

import com.trading212.judge.model.dto.DocumentSimpleDTO;
import com.trading212.judge.repository.task.DocumentRepository;
import com.trading212.judge.service.enums.DocumentDifficulty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.Set;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TaskService taskService;
    private final TransactionTemplate transactionTemplate;

    public DocumentService(DocumentRepository documentRepository, TaskService taskService, TransactionTemplate transactionTemplate) {
        this.documentRepository = documentRepository;
        this.taskService = taskService;
        this.transactionTemplate = transactionTemplate;
    }

    public Optional<Integer> findByName(String name) {
        return documentRepository.findByName(name);
    }

    public boolean create(String name, boolean isTest, DocumentDifficulty difficulty) {
        Optional<Integer> docId = documentRepository.findByName(name);

        if (docId.isPresent()) {
            return false;
        }

        //TODO: AWS send doc to S3

        return documentRepository.save(name, "someURL", difficulty, isTest);
    }

    public Set<DocumentSimpleDTO> getAll() {
        return documentRepository.getAll();
    }

    public boolean delete(Integer id) {
        transactionTemplate.getTransactionManager().getTransaction();
        return false;
    }
}
