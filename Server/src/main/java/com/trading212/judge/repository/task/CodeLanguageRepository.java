package com.trading212.judge.repository.task;

import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;

import java.util.Optional;

public interface CodeLanguageRepository {

    Optional<Integer> findIdByName(CodeLanguageEnum codeLanguage);
}
