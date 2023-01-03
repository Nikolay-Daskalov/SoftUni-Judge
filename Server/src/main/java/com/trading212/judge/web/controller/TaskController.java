package com.trading212.judge.web.controller;

import com.trading212.judge.model.dto.TaskByDescriptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {


    @GetMapping
    public ResponseEntity<TaskByDescriptionDTO> findByDescription(@RequestParam(defaultValue = "-1") Integer descriptionId) {

        //TODO:

        return null;
    }
}
