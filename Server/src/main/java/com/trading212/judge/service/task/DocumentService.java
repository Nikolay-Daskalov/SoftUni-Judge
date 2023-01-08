package com.trading212.judge.service.task;

import com.trading212.judge.model.dto.DocumentDTO;
import com.trading212.judge.model.dto.DocumentSimpleDTO;
import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.repository.task.DocumentRepository;
import com.trading212.judge.service.enums.DocumentDifficulty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.Set;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Optional<Integer> findByName(String name) {
        return documentRepository.findByName(name);
    }

    public boolean isExist(String name) {
        return documentRepository.isExist(name);
    }

    public Optional<DocumentDTO> create(String name, boolean isTest, DocumentDifficulty difficulty, File file) {

        //TODO: AWS send doc to S3

        Optional<Integer> docId = documentRepository.save(name, "someURL", difficulty, isTest);

        if (docId.isEmpty()) {
            return Optional.empty();
        }

        Optional<DocumentEntity> documentEntity = documentRepository.findById(docId.get());

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
        return documentRepository.delete(id);
    }
}
