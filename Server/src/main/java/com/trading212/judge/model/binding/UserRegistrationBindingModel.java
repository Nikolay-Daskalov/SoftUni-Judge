package com.trading212.judge.model.binding;

import com.trading212.judge.web.validation.NoSpecialCharacters;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationBindingModel(
        @NotBlank
        @Size(min = 3, max = 40)
        @NoSpecialCharacters
        String username,
        @NotBlank
        @NoSpecialCharacters
        @Email
        @Size(max = 254)
        String email,
        @NotBlank
        @Size(min = 3, max = 30)
        @NoSpecialCharacters
        String password,
        @NotBlank
        @Size(min = 3, max = 30)
        @NoSpecialCharacters
        String confirmPassword) {
}
