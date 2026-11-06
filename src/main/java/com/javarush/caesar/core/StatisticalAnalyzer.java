package com.javarush.caesar.core;

import com.javarush.caesar.exception.CipherException;

public class StatisticalAnalyzer {

    public String decryptByAnalyze(String encryptedText, char[] alphabet) {
        int bestSpaces = -1;
        String bestText = null;

        Cipher analyzeCipher = new Cipher(alphabet);

        for (int i = 1; i < alphabet.length; i++) {
            try {
                String text = analyzeCipher.decrypt(encryptedText, i);
                int spaces = findKeyBySpaces(text);

                if (spaces > bestSpaces) {
                    bestSpaces = spaces;
                    bestText = text;
                }

            } catch (CipherException e) {
                // Ключ i не подошёл — продолжаем перебор.
            }
        }

        return bestText;
    }


    private int findKeyBySpaces(String text) {

        int spaceCount = 0;

        for (char c : text.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
        }

        return spaceCount;
    }
}
