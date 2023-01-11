package com.trading212.judge.api;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExecutorServiceAPI {

    private static final String URL = "http:/localhost:8081/api/execute-code";

    private final RestTemplate restTemplate;

    public ExecutorServiceAPI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
