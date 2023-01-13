package com.trading212.judge.model.dto.task;

import com.trading212.judge.service.enums.CodeResultEnum;

public record CodeResultBindingModel(CodeResultEnum codeResult,  String executionTime) {
}
