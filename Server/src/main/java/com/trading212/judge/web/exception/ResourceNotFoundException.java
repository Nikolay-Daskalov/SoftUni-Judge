package com.trading212.judge.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Resource could not be found.";

    public ResourceNotFoundException() {
        super(MESSAGE);
    }
}
