package com.trading212.judge.service.task;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ExecutorServiceAPI {

    private static final String URL = "http://localhost:8081/api/execute-code";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ExecutorServiceAPI(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Boolean sendCode(String sourceCode, CodeLanguageEnum codeLanguage, List<String> testInputs, List<String> testOutputs) {
        String responseJSON = restTemplate.postForObject(URL, new CodeDataBindingModel(sourceCode, codeLanguage.name(), testInputs, testOutputs), String.class);


        CodeResultBindingModel codeResult = null;
        try {
            codeResult = objectMapper.readValue(responseJSON, CodeResultBindingModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (codeResult.codeResult() == null) {
            return null;
        }

        return null;
    }

    private record CodeDataBindingModel(
            String code,
            String language,
            List<String> testOutputs,
            List<String> testInputs) {
    }

    private enum CodeResultEnum {
        PASSED, FAILED
    }

    private record CodeResultBindingModel(CodeResultEnum codeResult) {

    }
}
