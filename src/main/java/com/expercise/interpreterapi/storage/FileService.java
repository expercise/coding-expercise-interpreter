package com.expercise.interpreterapi.storage;

import com.expercise.interpreterapi.exception.InterpreterException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public void write(String content, Path filePath) {
        try {
            if (Files.notExists(filePath.getParent())) {
                LOGGER.debug("Target directory '{}' not exists. It's being created...", filePath);
                Files.createDirectories(filePath.getParent());
                LOGGER.debug("Target directory '{}' has been created.", filePath);
            }
            LOGGER.debug("Writing content into file : {}", filePath);
            Files.write(filePath, content.getBytes(Charset.forName("UTF-8")));
            LOGGER.debug("Content has written into file : {}", filePath);
        } catch (IOException e) {
            throw new InterpreterException("Source could not be stored", e);
        }
    }

    public void delete(Path filePath) {
        LOGGER.debug("Deleting '{}' directory.", filePath);
        FileUtils.deleteQuietly(filePath.toFile());
        LOGGER.debug("Deleted '{}' directory.", filePath);
    }
}
