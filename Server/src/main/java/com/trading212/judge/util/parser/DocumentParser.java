package com.trading212.judge.util.parser;

import org.springframework.web.multipart.MultipartFile;

@FunctionalInterface
public interface DocumentParser {

    boolean parse(MultipartFile file);
}
