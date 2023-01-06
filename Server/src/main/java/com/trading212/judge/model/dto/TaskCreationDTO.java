package com.trading212.judge.model.dto;

import java.io.File;

public record TaskCreationDTO(
        String name,
        String documentName,
        File answersJSON) {
}
