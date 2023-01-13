package com.trading212.codeexecutor.service;

import com.trading212.codeexecutor.enums.CodeResultEnum;
import com.trading212.codeexecutor.enums.LanguageEnum;
import com.trading212.codeexecutor.model.dto.CodeResult;
import com.trading212.codeexecutor.model.dto.ProcessResult;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class CodeExecutionService {

    public CodeExecutionService() {
    }

    public CodeResult execute(String source, LanguageEnum language, List<String> testOutputs, List<String> testInputs) {
        try {
            File tempFile = createFile(source);

            ProcessService processService = new ProcessService();
            boolean canExecute = processService.checkIfCodeCanRun(tempFile, language);

            if (!canExecute) {
                tempFile.delete();
                return null;
            }

            ProcessResult result = processService.run(testInputs.toArray(String[]::new));

            tempFile.delete();
            deleteClassFile();

            return checkResult(result, testOutputs);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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

        ProcessBuilder deleteProcessBuilder = new ProcessBuilder("bash", "-c", "find . -type f -maxdepth 1  -name \"*.class\" -exec rm {} \\;");

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
