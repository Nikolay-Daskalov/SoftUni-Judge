package com.trading212.codeexecutor.model.dto;

import java.util.List;

public record ProcessResult(List<String> outputResults, String executionTime) {
}
