package com.trading212.judge.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExecutorServiceAPI {

    private static final String URL = "http:/localhost:8081/api/execute-code";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ExecutorServiceAPI(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Boolean sendCode(String sourceCode, CodeLanguageEnum codeLanguage) {
        String responseJSON = restTemplate.getForObject(URL, String.class);

        return null;
    }
}
