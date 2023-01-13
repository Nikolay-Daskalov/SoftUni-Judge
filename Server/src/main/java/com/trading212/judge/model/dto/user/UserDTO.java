package com.trading212.judge.model.dto.user;

public record UserDTO(
        Integer id,
        String username,
        String email,
        String createdAt) {
}
