package com.trading212.judge.model.dto.submission;

import com.trading212.judge.service.enums.CodeResultEnum;

import java.time.Instant;

public record SubmissionDTO(
        Integer id,
        Integer taskId,
        Integer userId,
        CodeResultEnum result,
        Integer codeLanguageId,
        String executionTime,
        Instant createdAt) {
}
