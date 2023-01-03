package com.trading212.judge.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AuthorizationHeaderNotValidException extends RuntimeException {
    public AuthorizationHeaderNotValidException(String message) {
        super(message);
    }
}
