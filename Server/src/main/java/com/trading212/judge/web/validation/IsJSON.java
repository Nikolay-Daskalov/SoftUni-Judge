package com.trading212.judge.web.validation;

import com.trading212.judge.web.validation.Impl.IsJSONValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = IsJSONValidator.class)
public @interface IsJSON {
    String message() default "File is not JSON format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
