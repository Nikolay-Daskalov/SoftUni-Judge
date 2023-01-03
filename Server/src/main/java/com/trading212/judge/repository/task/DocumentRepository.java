package com.trading212.judge.repository.task;

import com.trading212.judge.model.entity.task.DocumentEntity;

import java.util.Set;

public interface DocumentRepository {

    Set<DocumentEntity> getAll();
}
