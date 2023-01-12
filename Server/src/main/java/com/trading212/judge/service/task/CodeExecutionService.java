package com.trading212.judge.service.task;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.trading212.judge.model.dto.TaskDTO;
import com.trading212.judge.model.entity.task.enums.CodeLanguageEnum;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class CodeExecutionService {

    private final ExecutorServiceAPI executorServiceAPI;
    private final AmazonS3 amazonS3;
    private final TaskService taskService;

    public CodeExecutionService(ExecutorServiceAPI executorServiceAPI, AmazonS3 amazonS3, TaskService taskService) {
        this.executorServiceAPI = executorServiceAPI;
        this.amazonS3 = amazonS3;
        this.taskService = taskService;
    }

    public Boolean execute(String sourceCode, CodeLanguageEnum codeLanguage, Integer taskId, String username) {

        Optional<TaskDTO> taskById = taskService.findById(taskId);

        if (taskById.isEmpty()) {
            return null;
        }

        TaskDTO taskDTO = taskById.get();

        S3Object amazonS3Object = amazonS3.getObject("trading212-judge-submissions", taskDTO.answersURL());

        File file = null;
        try {
            file = File.createTempFile("taskAnswers", ".json");
            byte[] bytes = amazonS3Object.getObjectContent().readAllBytes();
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            for (byte aByte : bytes) {
                fileOutputStream.write(aByte);
            }

            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        Boolean codeResult = executorServiceAPI.sendCode(sourceCode, codeLanguage, null, null);

        if (codeResult == null) {
            return null;
        }

        if (codeResult) {
            //TODO:

            return true;
        }

        return false;
    }
}
