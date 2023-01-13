package com.trading212.judge.model.dto.task;

import java.time.Instant;

public record TaskDTO(
        Integer id,
        String name,
        Integer documentId,
        String answersURL,
        Instant createdAt) {
}
