package com.trading212.codeexecutor.service;

import com.trading212.codeexecutor.enums.LanguageEnum;
import com.trading212.codeexecutor.model.dto.ProcessResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ProcessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessService.class);

    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String PROGRAM_CLASS = "Solution";
    private static final Integer MAX_EXECUTION_TIME_SECONDS = 15;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final Pattern PATTERN = Pattern.compile(PROGRAM_CLASS + "\\W+\\w+\\.class");


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

                String classFiles = null;

                try (Stream<Path> pathStream = Files.list(Paths.get(TEMP_DIR))) {
                    classFiles = pathStream.filter(path -> {
                                Matcher matcher = PATTERN.matcher(path.getFileName().toString());

                                return matcher.find();
                            })
                            .map(path -> path.toFile().getName().split("\\.")[0])
                            .collect(Collectors.joining(" "));
                }


                ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", ".", PROGRAM_CLASS, classFiles);
                runProcessBuilder.directory(new File(TEMP_DIR));
                runProcessBuilder.redirectErrorStream(true);

                processBuilder = runProcessBuilder;
                return true;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.error("Language not valid");
            return false;
        }
    }

    public ProcessResult run(String... input) {
        try {
            Process startProcessExecution = processBuilder.start();

            Instant startTime = setUpInputs(startProcessExecution, input);

            startProcessExecution.waitFor(MAX_EXECUTION_TIME_SECONDS, TIME_UNIT);

            Instant endTime = Instant.now();

            if (startProcessExecution.exitValue() != 0) {
                return null;
            }

            List<String> outputs = getOutputs(startProcessExecution);

            long executionTimeMillis = startTime.until(endTime, ChronoUnit.MILLIS);

            return new ProcessResult(outputs, String.valueOf(executionTimeMillis / 1000.0));
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Code not valid!");
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    private List<String> getOutputs(Process process) throws IOException {
        List<String> outputs = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            outputs.add(line);
        }

        process.destroyForcibly();
        bufferedReader.close();

        return outputs;
    }

    private Instant setUpInputs(Process process, String... input) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        for (String currentInput : input) {
            bufferedWriter.write(currentInput);
            bufferedWriter.newLine();
        }

        Instant startTime = Instant.now();

        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ignored) {
        }

        return startTime;
    }
}
