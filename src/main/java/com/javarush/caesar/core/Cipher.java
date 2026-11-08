package com.javarush.caesar.core;

import com.javarush.caesar.exception.CipherException;

import java.util.HashMap;
import java.util.Map;

public class Cipher {

    private final char[] alphabet;
    private final Map<Character, Integer> indexChar = new HashMap<>();

    public Cipher(char[] alphabet) {
        this.alphabet = alphabet;
        for (int i = 0; i < alphabet.length; i++) {
            indexChar.put(alphabet[i], i);
        }
    }

    public String encrypt(String text, int shift) throws CipherException {
        if (text == null) {
            throw new CipherException("Файл пуст, обрабатывать нечего");
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Integer pos = indexChar.get(c);

            if (pos == null) {
                result.append(c);
            } else {
                result.append(alphabet[(pos + shift) % alphabet.length]);
            }
        }
        return result.toString();
    }

    public String decrypt(String encryptedText, int shift) throws CipherException {
        if (encryptedText == null) {
            throw new CipherException("Файл пуст, обрабатывать нечего");
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < encryptedText.length(); i++) {
            char c = encryptedText.charAt(i);
            Integer pos = indexChar.get(c);

            if (pos == null) {
                result.append(c);
            } else {
                result.append(alphabet[(pos - shift + alphabet.length) % alphabet.length]);
            }
        }
        return result.toString();

    }
}
