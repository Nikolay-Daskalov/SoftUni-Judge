package com.trading212.judge.web.validation;

import com.trading212.judge.web.validation.Impl.NoSpecialCharactersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NoSpecialCharactersValidator.class)
public @interface NoSpecialCharacters {
    String message() default "Field contains special characters or whitespaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
