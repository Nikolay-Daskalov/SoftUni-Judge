package com.trading212.codeexecutor.service;

import com.trading212.codeexecutor.enums.LanguageEnum;
import com.trading212.codeexecutor.model.dto.CodeResult;
import com.trading212.codeexecutor.model.dto.ProcessResult;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ProcessService {

    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String PROGRAM_CLASS = "Solution";
    private static final Integer MAX_EXECUTION_TIME_SECONDS = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private ProcessBuilder processBuilder;

    public ProcessService() {
        processBuilder = null;
    }

    public boolean checkIfCodeCanRun(File tempFile, LanguageEnum language) {
        if (language == LanguageEnum.JAVA) {
            ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", tempFile.getAbsolutePath());
            try {
                compileProcessBuilder.directory(new File(TEMP_DIR));
                Process compileProcess = compileProcessBuilder.start();
                compileProcess.waitFor(MAX_EXECUTION_TIME_SECONDS, TIME_UNIT);

                compileProcess.destroyForcibly();
                if (compileProcess.exitValue() != 0) {
                    return false;
                }

                ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", ".", PROGRAM_CLASS);
                runProcessBuilder.directory(new File(TEMP_DIR));
                runProcessBuilder.redirectErrorStream(true);

                processBuilder = runProcessBuilder;
                return true;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Language cannot be null!");
        }
    }

    public ProcessResult run(String... input) {
        try {
            Instant startTime = Instant.now();

            Process startProcessExecution = processBuilder.start();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(startProcessExecution.getOutputStream()));

            for (String currentInput : input) {
                if (currentInput == null) {
                    continue;//TODO:
                }
                bufferedWriter.write(currentInput);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

            startProcessExecution.waitFor(MAX_EXECUTION_TIME_SECONDS, TIME_UNIT);

            Instant endTime = Instant.now();

            if (startProcessExecution.exitValue() != 0) {
                return null;
            }

            List<String> outputs = new ArrayList<>();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(startProcessExecution.getInputStream()));

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                outputs.add(line);
            }

            startProcessExecution.destroyForcibly();
            bufferedReader.close();

            long executionTimeMillis = startTime.until(endTime, ChronoUnit.MILLIS);

            return new ProcessResult(outputs, String.valueOf(executionTimeMillis / 1000.0));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
