package com.trading212.judge.repository.task;

import com.trading212.judge.model.entity.task.SubmissionEntity;
import com.trading212.judge.service.enums.CodeResultEnum;

import java.util.Optional;

public interface SubmissionRepository {


    Integer save(Integer codeLanguageId, CodeResultEnum codeResult, Integer userId, Integer taskId, String executionTime);

    Optional<SubmissionEntity> findById(Integer id);
}
