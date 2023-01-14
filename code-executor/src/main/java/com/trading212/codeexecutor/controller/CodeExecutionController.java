package com.trading212.codeexecutor.controller;

import com.trading212.codeexecutor.model.binding.CodeDataBindingModel;
import com.trading212.codeexecutor.model.dto.CodeResult;
import com.trading212.codeexecutor.model.view.CodeExecutionResultViewModel;
import com.trading212.codeexecutor.service.CodeExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.trading212.codeexecutor.controller.CodeExecutionController.Routes;

@RestController
@RequestMapping(path = Routes.BASE)
public class CodeExecutionController {

    private final CodeExecutionService codeExecutionService;

    public CodeExecutionController(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }


    @PostMapping
    public ResponseEntity<CodeExecutionResultViewModel> postCode(@RequestBody CodeDataBindingModel codeDataBindingModel) {

        CodeResult codeResult = codeExecutionService.execute(
                codeDataBindingModel.code(),
                codeDataBindingModel.language(),
                codeDataBindingModel.answers());

        if (codeResult == null) {
            return ResponseEntity.badRequest().build();
        }

        CodeExecutionResultViewModel codeExecutionResultViewModel = new CodeExecutionResultViewModel(codeResult.codeResult(), codeResult.executionTime());

        return ResponseEntity.ok(codeExecutionResultViewModel);
    }

    static class Routes {
        static final String BASE = "/api/execute-code";
    }
}
