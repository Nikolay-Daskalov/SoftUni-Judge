package com.trading212.judge.model.dto.task;

import com.trading212.judge.service.enums.CodeResultEnum;

public record CodeResultDTO(
        Integer id,
        CodeResultEnum codeResult,
        String executionTime) {
}
