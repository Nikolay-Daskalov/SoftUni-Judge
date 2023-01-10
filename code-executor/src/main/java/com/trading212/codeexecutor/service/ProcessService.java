package com.trading212.codeexecutor.service;

import com.trading212.codeexecutor.enums.LanguageEnum;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class ProcessService {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String PROGRAM_CLASS = "Solution";
    private static final Integer MAX_EXECUTION_TIME_SECONDS = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private Process process;

    public ProcessService() {
        process = null;
    }

    public void runFileCode(File tempFile, LanguageEnum language) {
        if (language == LanguageEnum.JAVA) {
            ProcessBuilder pb = new ProcessBuilder("java", tempFile.getAbsolutePath());
            try {
                Process p = pb.start();
                p.waitFor(MAX_EXECUTION_TIME_SECONDS, TIME_UNIT);

                if (p.exitValue() != 0) {
                    return null;
                }

                ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", ".", PROGRAM_CLASS);
                processBuilder.directory(new File(TEMP_DIR));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
