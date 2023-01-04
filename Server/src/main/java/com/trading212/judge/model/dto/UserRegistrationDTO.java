package com.trading212.judge.model.dto;

public record UserRegistrationDTO(
        String username,
        String email,
        String password) {
}
