package com.trading212.judge.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trading212.judge.service.enums.DocumentDifficulty;

import java.time.Instant;

public record DocumentDTO(
        Integer id,
        String name,
        String url,
        DocumentDifficulty difficulty,
        boolean isTest,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Instant createdAt) {
}
