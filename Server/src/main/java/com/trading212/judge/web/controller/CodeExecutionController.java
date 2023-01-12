package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.CodeBindingModel;
import com.trading212.judge.service.task.CodeExecutionService;
import com.trading212.judge.service.task.TaskService;
import com.trading212.judge.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.trading212.judge.web.controller.CodeExecutionController.Routes;

@RestController
@RequestMapping(path = Routes.BASE)
public class CodeExecutionController {

    private final CodeExecutionService codeExecutionService;

    public CodeExecutionController(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }


    @PostMapping
    public ResponseEntity<?> uploadCode(@RequestBody @Valid CodeBindingModel codeBindingModel, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Boolean codeResult = codeExecutionService.execute(codeBindingModel.sourceCode(), codeBindingModel.codeLanguage(), codeBindingModel.taskId(), principal.getName());

        if (codeResult == null) {
            throw new RuntimeException();//TODO
        }

        return ResponseEntity.notFound().build();
    }


    public static class Routes {
        public static final String BASE = "/api/code";
    }
}
