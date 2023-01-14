package com.trading212.judge.api.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.trading212.judge.api.CloudStorageAPI;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class AWSCloudStorageAPI implements CloudStorageAPI {

    private static final String BUCKET = "trading212-judge-submissions";
    private static final String URL = "https://trading212-judge-submissions.s3.eu-west-3.amazonaws.com/";

    private final AmazonS3 amazonS3;

    public AWSCloudStorageAPI(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String createDocument(String name, File file) {
        final String objectKey = name + ".docx";

        amazonS3.putObject(BUCKET, objectKey, file);

        return objectKey;
    }

    @Override
    public String createAnswers(String name, File file) {
        final String objectKey = name + "-answers.json";

        amazonS3.putObject(BUCKET, objectKey, file);

        return objectKey;
    }

    @Override
    public File getAnswersObject(String objectKey) {
        S3Object amazonS3Object = amazonS3.getObject(BUCKET, objectKey);

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

        return file;
    }

    @Override
    public String getObjectURL(String objectKey) {
        return URL + objectKey;
    }
}
