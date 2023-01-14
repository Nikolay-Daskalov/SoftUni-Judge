package com.trading212.judge.model.dto.task;

import java.util.Set;

public record TaskPageable(Set<TaskSimpleDTO> task, Integer totalPages) {
}
