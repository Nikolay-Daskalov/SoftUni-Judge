package com.trading212.judge.service.task;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading212.judge.model.dto.task.CodeResultBindingModel;
import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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

    public CodeResultBindingModel sendCode(String sourceCode, CodeLanguageEnum codeLanguage, List<String> testInputs, List<String> testOutputs) {
        ResponseEntity<String> codeResponse = null;
        try {
            codeResponse = restTemplate.postForEntity(URL, new CodeDataDTO(sourceCode, codeLanguage.name(), testOutputs, testInputs), String.class);
        } catch (RestClientException ignored) {
            return null;
        }

        try {
            return objectMapper.readValue(codeResponse.getBody(), CodeResultBindingModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private record CodeDataDTO(
            String code,
            String language,
            List<String> testOutputs,
            List<String> testInputs) {
    }
}
