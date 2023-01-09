package com.trading212.judge.util.parser.Impl;

import com.trading212.judge.util.parser.DocumentParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipException;

@Component
public class DocumentParserImpl implements DocumentParser {

    public DocumentParserImpl() {
    }

    @Override
    public boolean parse(MultipartFile file) {
        File tempDoc = null;

        try {
            tempDoc = File.createTempFile("doc", "docx");

            FileOutputStream tempDocOutputStream = new FileOutputStream(tempDoc);

            tempDocOutputStream.write(file.getBytes());

            tempDocOutputStream.flush();
            tempDocOutputStream.close();

            XWPFDocument xwpfDocument = new XWPFDocument(OPCPackage.open(tempDoc));
            xwpfDocument.close();
            tempDoc.delete();

            return true;
        } catch (InvalidFormatException | NotOfficeXmlFileException | IOException e) {
            if (tempDoc != null) {
                tempDoc.delete();
            }

            return false;
        }
    }
}
