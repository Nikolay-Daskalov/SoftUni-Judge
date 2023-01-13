package com.trading212.codeexecutor.model.dto;

import com.trading212.codeexecutor.enums.CodeResultEnum;

public record CodeResult(CodeResultEnum codeResult, String executionTime) {
}
