package com.trading212.judge.repository.submission;

import com.trading212.judge.model.entity.submission.SubmissionEntity;
import com.trading212.judge.service.enums.CodeResultEnum;

import java.util.Optional;
import java.util.Set;

public interface SubmissionRepository {

    Integer save(Integer codeLanguageId, CodeResultEnum codeResult, Integer userId, Integer taskId, String executionTime);

    Optional<SubmissionEntity> findById(Integer id);

    boolean deleteAllByTasksIds(Set<Integer> tasksIds);
}
