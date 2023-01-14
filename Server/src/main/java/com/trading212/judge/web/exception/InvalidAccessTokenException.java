package com.trading212.judge.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class InvalidAccessTokenException extends RuntimeException {

    private static final String MESSAGE = "Access token is invalid.";

    public InvalidAccessTokenException() {
        super(MESSAGE);
    }
}
