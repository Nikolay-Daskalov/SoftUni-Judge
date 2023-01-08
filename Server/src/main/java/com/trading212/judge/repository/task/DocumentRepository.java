package com.trading212.judge.repository.task;

import com.trading212.judge.model.dto.DocumentDTO;
import com.trading212.judge.model.dto.DocumentSimpleDTO;
import com.trading212.judge.model.entity.task.DocumentEntity;
import com.trading212.judge.service.enums.DocumentDifficulty;

import java.util.Optional;
import java.util.Set;

public interface DocumentRepository {

    Set<DocumentSimpleDTO> getAll();

    Optional<Integer> findByName(String name);

    Optional<DocumentEntity> findById(Integer id);

    Optional<Integer> save(String name, String docURL, DocumentDifficulty difficulty, boolean isTest);

    boolean isExist(String name);

    boolean delete(Integer id);
}
