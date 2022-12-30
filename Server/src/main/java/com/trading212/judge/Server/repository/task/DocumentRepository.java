package com.trading212.judge.Server.repository.task;

import com.trading212.judge.Server.model.entity.task.DocumentEntity;

import java.util.Set;

public interface DocumentRepository {

    Set<DocumentEntity> getAll();
}
