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

        StringBuilder rezult = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Integer pos = indexChar.get(c);

            if (pos == null) {
                rezult.append(c);
            } else {
                if ((pos + shift) > alphabet.length) {
                    rezult.append(alphabet[pos + shift - alphabet.length]);
                } else {
                    rezult.append(alphabet[pos + shift]);
                }
            }
        }
        return rezult.toString();
    }

    public String decrypt(String encryptedText, int shift) throws CipherException {
        if (encryptedText == null) {
            throw new CipherException("Файл пуст, обрабатывать нечего");
        }

        StringBuilder rezult = new StringBuilder();

        for (int i = 0; i < encryptedText.length(); i++) {
            char c = encryptedText.charAt(i);
            Integer pos = indexChar.get(c);

            if (pos == null) {
                throw new CipherException("Символа '" + c + "' нет а алфавите");
            } else {
                if ((pos - shift) < 0) {
                    rezult.append(alphabet[pos - shift + alphabet.length]);
                } else {
                    rezult.append(alphabet[pos - shift]);
                }
            }
        }
        return rezult.toString();

    }
}
