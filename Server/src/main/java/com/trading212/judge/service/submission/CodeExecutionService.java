package com.trading212.judge.service.submission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading212.judge.api.CloudStorageAPI;
import com.trading212.judge.model.dto.submission.*;
import com.trading212.judge.model.dto.task.*;
import com.trading212.judge.model.entity.submission.enums.CodeLanguageEnum;
import com.trading212.judge.service.task.TaskService;
import com.trading212.judge.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CodeExecutionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeExecutionService.class);

    private final ExecutorServiceAPI executorServiceAPI;
    private final TaskService taskService;
    private final ObjectMapper objectMapper;
    private final SubmissionService submissionService;
    private final UserService userService;
    private final CodeLanguageService codeLanguageService;
    private final CloudStorageAPI cloudStorageAPI;

    public CodeExecutionService(ExecutorServiceAPI executorServiceAPI, TaskService taskService, ObjectMapper objectMapper, SubmissionService submissionService, UserService userService, CodeLanguageService codeLanguageService, CloudStorageAPI cloudStorageAPI) {
        this.executorServiceAPI = executorServiceAPI;
        this.taskService = taskService;
        this.objectMapper = objectMapper;
        this.submissionService = submissionService;
        this.userService = userService;
        this.codeLanguageService = codeLanguageService;
        this.cloudStorageAPI = cloudStorageAPI;
    }

    public CodeResultDTO execute(String sourceCode, CodeLanguageEnum codeLanguage, Integer taskId, String username) {
        Optional<TaskDTO> taskById = taskService.findById(taskId);
        if (taskById.isEmpty()) {
            return null;
        }

        Optional<Integer> userId = userService.getIdByUsername(username);
        if (userId.isEmpty()) {
            return null;
        }

        Optional<Integer> codeLanguageId = codeLanguageService.findIdByName(codeLanguage);
        if (codeLanguageId.isEmpty()) {
            return null;
        }

        File file = cloudStorageAPI.getAnswersObject(taskById.get().answersURL());
        TaskAnswersDTO taskAnswersDTO = mapFileToPOJO(file);
        file.delete();

        if (taskAnswersDTO == null) {
            return null;
        }

        List<AnswerCases> answersList = new ArrayList<>(taskAnswersDTO.cases());

        CodeResultBindingModel codeResult = executorServiceAPI.sendCode(sourceCode, codeLanguage, answersList);
        if (codeResult == null) {
            return null;
        }

        Integer submissionId = submissionService.save(codeLanguageId.get(), codeResult.codeResult(), userId.get(), taskId, codeResult.executionTime());
        return mapSubmissionDTO(submissionService.findById(submissionId).get());
    }

    private CodeResultDTO mapSubmissionDTO(SubmissionDTO submissionDTO) {
        return new CodeResultDTO(
                submissionDTO.id(),
                submissionDTO.result(),
                submissionDTO.executionTime()
        );
    }

    private TaskAnswersDTO mapFileToPOJO(File file) {
        TaskAnswersDTO taskAnswersDTO = null;

        try {
            taskAnswersDTO = objectMapper.readValue(file, TaskAnswersDTO.class);
        } catch (IOException e) {
            LOGGER.error("JSON file cannot be mapped to POJO.");
            return null;
        }

        return taskAnswersDTO;
    }
}
