package com.trading212.judge.model.dto.task;

import com.trading212.judge.service.enums.DocumentDifficulty;

import java.time.Instant;

public record DocumentDTO(
        Integer id,
        String name,
        String url,
        DocumentDifficulty difficulty,
        boolean isTest,
        Instant createdAt) {
}
