package com.trading212.judge.web.exception.task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TaskExistException extends RuntimeException {
    public TaskExistException(String message) {
        super(message);
    }
}
