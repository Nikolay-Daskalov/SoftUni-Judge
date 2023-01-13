package com.trading212.judge.web.validation.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading212.judge.model.dto.task.TaskAnswersJSON;
import com.trading212.judge.web.validation.IsJSON;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class IsJSONValidator implements ConstraintValidator<IsJSON, MultipartFile> {

    private final ObjectMapper objectMapper;

    public IsJSONValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean isExtensionValid = isExtensionValid(value.getOriginalFilename());

        if (isExtensionValid) {
            return isJSON(value);
        }

        return false;
    }

    private boolean isExtensionValid(String filename) {
        String extensionToCompare = "json";

        String[] parts = filename.split("\\.");
        String extension = parts[parts.length - 1];

        return extension.equals(extensionToCompare);
    }

    private boolean isJSON(MultipartFile file) {
        try {
            File fileToValidate = File.createTempFile("answers", ".json");
            Files.write(fileToValidate.toPath(), file.getBytes());

            objectMapper.readValue(fileToValidate, TaskAnswersJSON.class);

            fileToValidate.delete();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
