package com.javarush.caesar.service;

import java.nio.file.Files;
import java.nio.file.Path;

public class Validator {

    public boolean isValidKey(int key, char[] alphabet) {
        return  (key >= 0 && key < alphabet.length);

    }

    public boolean isFileExists(String filePath) {
        return (Files.isRegularFile(Path.of(filePath)));
    }
}
