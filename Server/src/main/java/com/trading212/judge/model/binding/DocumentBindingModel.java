package com.trading212.judge.model.binding;

import com.trading212.judge.service.enums.DocumentDifficulty;
import com.trading212.judge.web.validation.IsDocx;
import com.trading212.judge.web.validation.NoSpecialCharacters;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record DocumentBindingModel(
        @NotNull
        @NoSpecialCharacters
        @Size(max = 50)
        String name,
        @NotNull
        DocumentDifficulty difficulty,
        @NotNull
        Boolean isTest,
        @NotNull
        @IsDocx
        MultipartFile document) {
}
