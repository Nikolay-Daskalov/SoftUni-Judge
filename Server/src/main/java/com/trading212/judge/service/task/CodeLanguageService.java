package com.trading212.judge.service.task;

import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;
import com.trading212.judge.repository.task.CodeLanguageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CodeLanguageService {

    private final CodeLanguageRepository codeLanguageRepository;

    public CodeLanguageService(CodeLanguageRepository codeLanguageRepository) {
        this.codeLanguageRepository = codeLanguageRepository;
    }

    public Optional<Integer> findIdByName(CodeLanguageEnum codeLanguage) {
        return codeLanguageRepository.findIdByName(codeLanguage);
    }
}
