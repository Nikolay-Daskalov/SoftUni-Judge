package com.trading212.judge.model.dto.task;

import com.trading212.judge.service.enums.DocumentDifficulty;

public record DocumentSimpleDTO(Integer id, String name, DocumentDifficulty difficulty, boolean isTest) {

}
