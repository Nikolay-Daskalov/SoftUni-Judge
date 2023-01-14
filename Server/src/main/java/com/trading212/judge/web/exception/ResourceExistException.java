package com.trading212.judge.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ResourceExistException extends RuntimeException {

    private static final String MESSAGE = "Resource already exists.";

    public ResourceExistException() {
        super(MESSAGE);
    }
}
