package com.trading212.judge.model.dto.user;

public record UserRegistrationDTO(
        String username,
        String email,
        String password) {
}
