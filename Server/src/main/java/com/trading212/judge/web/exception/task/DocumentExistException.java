package com.trading212.judge.web.exception.task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DocumentExistException extends RuntimeException {
    public DocumentExistException(String message) {
        super(message);
    }
}
