package com.trading212.judge.web.validation;

import com.trading212.judge.web.validation.Impl.IsDocxValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = IsDocxValidator.class)
public @interface IsDocx {
    String message() default "File is not DOCX format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
