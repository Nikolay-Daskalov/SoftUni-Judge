package com.trading212.judge.service.submission;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading212.judge.model.dto.submission.AnswerCases;
import com.trading212.judge.model.dto.submission.CodeResultBindingModel;
import com.trading212.judge.model.entity.submission.enums.CodeLanguageEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ExecutorServiceAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorServiceAPI.class);
    private static final String URL = "http://localhost:8081/api/execute-code";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ExecutorServiceAPI(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public CodeResultBindingModel sendCode(String sourceCode, CodeLanguageEnum codeLanguage, List<AnswerCases> answers) {
        ResponseEntity<String> codeResponse = null;
        try {
            codeResponse = restTemplate.postForEntity(URL, new CodeDataDTO(sourceCode, codeLanguage.name(), answers), String.class);
        } catch (RestClientException ignored) {
            //Rest template throws exception for 400 status code
            return null;
        }

        try {
            return objectMapper.readValue(codeResponse.getBody(), CodeResultBindingModel.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("JSON parsing from Executor service failed!");
            return null;
        }
    }

    private record CodeDataDTO(
            String code,
            String language,
            List<AnswerCases> answers) {
    }
}
