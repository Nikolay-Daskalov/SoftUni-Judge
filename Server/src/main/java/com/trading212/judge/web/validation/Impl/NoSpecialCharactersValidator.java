package com.trading212.judge.web.validation.Impl;

import com.trading212.judge.web.validation.NoSpecialCharacters;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NoSpecialCharactersValidator implements ConstraintValidator<NoSpecialCharacters, String> {

    private static final Set<Character> specialCharacters = Set.of('<', '>', '\\', '/', '"', '\'', '`', '%', '&', '=', ';', '|', ',', ':');

    public NoSpecialCharactersValidator() {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.chars().noneMatch(character -> Character.isWhitespace(character) || specialCharacters.contains((char) character));
    }
}
