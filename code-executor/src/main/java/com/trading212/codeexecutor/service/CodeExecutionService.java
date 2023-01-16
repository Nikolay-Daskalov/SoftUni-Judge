package com.trading212.codeexecutor.service;

import com.trading212.codeexecutor.enums.CodeResultEnum;
import com.trading212.codeexecutor.enums.LanguageEnum;
import com.trading212.codeexecutor.model.binding.AnswerCases;
import com.trading212.codeexecutor.model.dto.CodeResult;
import com.trading212.codeexecutor.model.dto.ProcessResult;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeExecutionService {

    public CodeExecutionService() {
    }

    public CodeResult execute(String source, LanguageEnum language, List<AnswerCases> answers) {
        try {
            File tempFile = createFile(source);

            ProcessService processService = new ProcessService();
            boolean canExecute = processService.checkIfCodeCanRun(tempFile, language);

            if (!canExecute) {
                tempFile.delete();
                return null;
            }

            List<String> executionTimeHolder = new ArrayList<>();
            for (AnswerCases answer : answers) {
                CodeResult codeResult;

                ProcessResult processResult;
                if (answer.inputs() == null) {
                    processResult = processService.run();
                } else {
                    processResult = processService.run(answer.inputs().toArray(String[]::new));
                }

                if (processResult == null) {
                    return null;
                }

                codeResult = checkResult(processResult, answer.outputs());

                executionTimeHolder.add(codeResult.executionTime());
                if (codeResult.codeResult() == CodeResultEnum.UNSOLVED) {
                    return getResult(executionTimeHolder, CodeResultEnum.UNSOLVED);
                }
            }

            tempFile.delete();
            deleteClassFile();

            return getResult(executionTimeHolder, CodeResultEnum.SOLVED);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private CodeResult getResult(List<String> executionTimeHolder, CodeResultEnum codeResult) {
        double avgExecutionTime = executionTimeHolder.stream()
                .mapToDouble(Double::valueOf)
                .average().getAsDouble();

        return new CodeResult(codeResult, String.valueOf(avgExecutionTime));
    }

    private CodeResult checkResult(ProcessResult processResult, List<String> testOutputs) {
        List<String> outputs = processResult.outputResults();

        if (outputs.size() != testOutputs.size()) {
            return new CodeResult(CodeResultEnum.UNSOLVED, processResult.executionTime());
        }

        for (int i = 0; i < testOutputs.size(); i++) {
            if (!testOutputs.get(i).equals(outputs.get(i))) {
                return new CodeResult(CodeResultEnum.UNSOLVED, processResult.executionTime());
            }
        }

        return new CodeResult(CodeResultEnum.SOLVED, processResult.executionTime());
    }

    private void deleteClassFile() throws IOException, InterruptedException {
        String tempDir = ProcessService.TEMP_DIR;

        ProcessBuilder deleteProcessBuilder = new ProcessBuilder("bash", "-c", "rm *.class");

        deleteProcessBuilder.directory(new File(tempDir));

        Process deleteProcess = deleteProcessBuilder.start();

        deleteProcess.waitFor();

        deleteProcess.destroyForcibly();
    }

    private File createFile(String source) throws IOException {
        String prefix = "solution";
        String suffix = ".java";

        File tempFile = File.createTempFile(prefix, suffix);

        BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)));

        bf.write(source);
        bf.close();

        return tempFile;
    }
}
