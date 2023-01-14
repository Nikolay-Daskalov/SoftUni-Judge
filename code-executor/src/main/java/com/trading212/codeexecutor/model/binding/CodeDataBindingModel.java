package com.trading212.codeexecutor.model.binding;

import com.trading212.codeexecutor.enums.LanguageEnum;

import java.util.List;

public record CodeDataBindingModel(
        String code,
        LanguageEnum language,
        List<AnswerCases> answers) {
}
