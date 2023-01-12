package com.trading212.judge.model.binding;

import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CodeBindingModel(
        @NotNull
        CodeLanguageEnum codeLanguage,
        @NotBlank
        String sourceCode,
        @NotNull
        Integer taskId) {
}
