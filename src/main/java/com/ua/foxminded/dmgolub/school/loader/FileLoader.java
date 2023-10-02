package com.ua.foxminded.dmgolub.school.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLoader implements Loader<List<String>> {
    
    private final String fileName;
    
    public FileLoader(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("File name can not be null");
        }
        if (fileName.isEmpty()) {
            throw new IllegalArgumentException("File name can not be an empty string");
        }
        this.fileName = fileName;
    }
    
    @Override
    public List<String> load() {
        try (Stream<String> streamFromFile = Files.lines(Paths.get(fileName))) {
            return streamFromFile.collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Unable to open file: " + e.getMessage());
            throw new IllegalStateException(e);
        }
    }
}