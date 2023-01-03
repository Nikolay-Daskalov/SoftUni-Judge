package com.trading212.judge.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Task Id is <= 0")
public class DocumentIdNotPositiveException extends RuntimeException {
    public DocumentIdNotPositiveException() {
        super();
    }
}
