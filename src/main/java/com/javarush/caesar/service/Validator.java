package com.javarush.caesar.service;

import com.javarush.caesar.exception.CipherException;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Validator {

    public boolean isValidKey(int key, char[] alphabet) {
        return  (key >= 0 && key < alphabet.length);

    }

    public boolean isFileExists(String filePath) throws CipherException {

        if (filePath == null || filePath.isBlank()) {
            return false;
        }

        try {
            Path file = Path.of(filePath).toAbsolutePath().normalize();
            return Files.exists(file) && Files.isRegularFile(file);
        } catch (InvalidPathException e) {
           throw new CipherException("Путь '" + filePath + "' содержит недопустимые символы");
        }
    }
}
