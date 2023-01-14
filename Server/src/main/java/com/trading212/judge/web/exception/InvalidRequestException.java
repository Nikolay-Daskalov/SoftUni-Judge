package com.trading212.judge.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
    private static final String MESSAGE = "Invalid field data.";

    public InvalidRequestException() {
        super(MESSAGE);
    }
}
