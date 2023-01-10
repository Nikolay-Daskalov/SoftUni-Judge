package com.trading212.codeexecutor.model.binding;

import com.trading212.codeexecutor.enums.LanguageEnum;

public record CodeDataBindingModel(String code, LanguageEnum language) {
}
