package com.trading212.judge.model.dto.task;

import java.util.Set;

public record DocumentPageable(Set<DocumentSimpleDTO> documents, Integer totalPages) {
}
