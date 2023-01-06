package com.trading212.judge.model.binding;

import com.trading212.judge.web.validation.IsJSON;
import com.trading212.judge.web.validation.NoSpecialCharacters;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record TaskBindingModel(
        @NotBlank
        @NoSpecialCharacters
        String name,
        @NotBlank
        @NoSpecialCharacters
        String documentName,
        @IsJSON
        MultipartFile answers) {
}
