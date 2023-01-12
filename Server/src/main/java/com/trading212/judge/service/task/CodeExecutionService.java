package com.trading212.judge.service.task;

import com.trading212.judge.api.ExecutorServiceAPI;
import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;
import org.springframework.stereotype.Service;

@Service
public class CodeExecutionService {

    private final ExecutorServiceAPI executorServiceAPI;

    public CodeExecutionService(ExecutorServiceAPI executorServiceAPI) {
        this.executorServiceAPI = executorServiceAPI;
    }


    public Boolean execute(String sourceCode, CodeLanguageEnum codeLanguage, String username) {
        Boolean codeResult = executorServiceAPI.sendCode(sourceCode, codeLanguage);

        if (codeResult == null) {
            return null;
        }

        if (codeResult) {
            //TODO:

            return true;
        }

        return false;
    }
}
