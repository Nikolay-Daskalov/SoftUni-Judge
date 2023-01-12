package com.trading212.judge.service.task;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.trading212.judge.model.dto.DocumentDTO;
import com.trading212.judge.model.dto.DocumentSimpleDTO;
import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.repository.task.DocumentRepository;
import com.trading212.judge.service.enums.DocumentDifficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.util.Optional;
import java.util.Set;

@Service
public class DocumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final TransactionTemplate transactionTemplate;
    private final TaskService taskService;
    private final AmazonS3 amazonS3;

    public DocumentService(DocumentRepository documentRepository, TransactionTemplate transactionTemplate, TaskService taskService, AmazonS3 amazonS3) {
        this.documentRepository = documentRepository;
        this.transactionTemplate = transactionTemplate;
        this.taskService = taskService;
        this.amazonS3 = amazonS3;
    }

    public Optional<DocumentDTO> findByID(String name) {
        Optional<DocumentEntity> docByName = documentRepository.findByName(name);

        return mapToDTO(docByName);
    }

    public Optional<DocumentDTO> findByID(Integer id) {
        Optional<DocumentEntity> documentEntity = documentRepository.findById(id);

        return mapToDTO(documentEntity);
    }

    public boolean isExist(String name) {
        return documentRepository.isExist(name);
    }

    public boolean isExist(Integer id) {
        return documentRepository.isExist(id);
    }

    public Optional<DocumentDTO> create(String name, boolean isTest, DocumentDifficulty difficulty, File file) {

        amazonS3.putObject("trading212-judge-submissions", name + ".docx", file);

        String docUrl = amazonS3.getUrl("trading212-judge-submissions", name).toString();

        Integer docId = documentRepository.save(name, docUrl, difficulty, isTest);

        if (docId == null) {
            return Optional.empty();
        }

        Optional<DocumentEntity> documentEntity = documentRepository.findById(docId);

        return mapToDTO(documentEntity);
    }

    private Optional<DocumentDTO> mapToDTO(Optional<DocumentEntity> documentEntity) {
        if (documentEntity.isEmpty()) {
            return Optional.empty();
        }

        DocumentEntity entity = documentEntity.get();

        DocumentDTO documentDTO = new DocumentDTO(
                entity.getId(),
                entity.getName(),
                entity.getUrl(),
                entity.getDifficulty(),
                entity.isTest(),
                entity.getCreatedAt()
        );

        return Optional.of(documentDTO);
    }


    public Set<DocumentSimpleDTO> getAll() {
        return documentRepository.getAll();
    }

    public boolean delete(Integer id) {
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    taskService.deleteAllByDocument(id);

                    documentRepository.delete(id);
                }
            });

            return true;
        } catch (TransactionException e) {
            LOGGER.error("Transaction failed for deleting document and it`s tasks!");
            return false;
        }
    }
}
