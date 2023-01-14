package com.trading212.judge.web.controller;

import com.trading212.judge.model.binding.CodeBindingModel;
import com.trading212.judge.model.dto.submission.CodeResultDTO;
import com.trading212.judge.service.submission.CodeExecutionService;
import com.trading212.judge.util.path.ResourcePathUtil;
import com.trading212.judge.web.exception.InvalidRequestException;
import jakarta.servlet.http.HttpServletRequest;
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
    private final ResourcePathUtil resourcePathUtil;

    public CodeExecutionController(CodeExecutionService codeExecutionService, ResourcePathUtil resourcePathUtil) {
        this.codeExecutionService = codeExecutionService;
        this.resourcePathUtil = resourcePathUtil;
    }


    @PostMapping
    public ResponseEntity<CodeResultDTO> uploadCode(@RequestBody @Valid CodeBindingModel codeBindingModel,
                                                    BindingResult bindingResult, Principal principal, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }

        CodeResultDTO codeResultDTO = codeExecutionService.execute(codeBindingModel.sourceCode(), codeBindingModel.codeLanguage(), codeBindingModel.taskId(), principal.getName());

        if (codeResultDTO == null) {
            throw new InvalidRequestException();
        }

        return ResponseEntity.created(resourcePathUtil.buildResourcePath(httpServletRequest, codeResultDTO.id()))
                .body(codeResultDTO);
    }


    public static class Routes {
        public static final String BASE = "/api/code";
    }
}
