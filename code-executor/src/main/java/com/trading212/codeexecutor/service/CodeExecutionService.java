package com.trading212.codeexecutor.service;

import com.trading212.codeexecutor.enums.CodeResultEnum;
import com.trading212.codeexecutor.enums.LanguageEnum;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Service
public class CodeExecutionService {

    private static final String PROGRAM_CLASS = "Solution";
    private static final Integer MAX_EXECUTION_TIME_SECONDS = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    public CodeExecutionService() {
    }

    public CodeResultEnum execute(String source, LanguageEnum language) {
        try {
            File tempFile = createFile(source);

            ProcessBuilder pb = new ProcessBuilder("javac", tempFile.getAbsolutePath());
            Process p = pb.start();

            boolean isExecuted = ls.waitFor(10, TimeUnit.SECONDS);

            if (!isExecuted) {
                ls.destroyForcibly();
            }

            System.out.println(ls.exitValue());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ls.getInputStream()));

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

            bufferedReader.close();

            tempFile.delete();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return null;
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
