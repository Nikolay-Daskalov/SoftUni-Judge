package com.trading212.judge.api;

import java.io.File;

public interface CloudStorageAPI {

    String createDocument(String name, File file);

    String createAnswers(String name, File file);

    File getAnswersObject(String objectKey);

    String getObjectURL(String objectKey);
}
