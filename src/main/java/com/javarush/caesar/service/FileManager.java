package com.javarush.caesar.service;

import com.javarush.caesar.exception.CipherException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    public String readFile(String filePath) throws CipherException {
        try {
            return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CipherException("Не удалось прочитать файл, ошибка: ", e);
        }
    }

    public void writeFile(String content, String filePath) throws CipherException {
        try {
            Files.writeString(Path.of(filePath), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CipherException("Не удалось записать файл, ошибка: ", e);
        }
    }
}
