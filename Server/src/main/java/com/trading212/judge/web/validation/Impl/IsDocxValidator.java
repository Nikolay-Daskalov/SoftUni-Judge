package com.trading212.judge.web.validation.Impl;

import com.trading212.judge.util.parser.DocumentParser;
import com.trading212.judge.web.validation.IsDocx;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class IsDocxValidator implements ConstraintValidator<IsDocx, MultipartFile> {

    private final DocumentParser documentParser;

    public IsDocxValidator(DocumentParser documentParser) {
        this.documentParser = documentParser;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null) {
            return true;
        }

        return documentParser.parse(file);
    }
}
