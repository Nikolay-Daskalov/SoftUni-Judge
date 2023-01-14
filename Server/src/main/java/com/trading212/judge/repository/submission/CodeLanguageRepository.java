package com.trading212.judge.repository.submission;

import com.trading212.judge.model.entity.submission.enums.CodeLanguageEnum;

import java.util.Optional;

public interface CodeLanguageRepository {

    Optional<Integer> findIdByName(CodeLanguageEnum codeLanguage);
}
