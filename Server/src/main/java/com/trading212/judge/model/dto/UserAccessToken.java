package com.trading212.judge.model.dto;

import java.util.List;

public record UserAccessToken(
        String username,
        List<String> roles) {
}
