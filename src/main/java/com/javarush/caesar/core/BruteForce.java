package com.javarush.caesar.core;

import com.javarush.caesar.exception.CipherException;

import java.util.Set;

public class BruteForce {

    private static final Set<String> COMMON = Set.of(
            "и", "в", "не", "на", "с", "я", "к", "у", "о", "а",
            "но", "да", "то", "же", "бы", "ли", "за", "по", "от",
            "до", "из", "ни", "мы", "вы", "ты", "он", "она", "они"
    );

    public String decryptByBruteForce(String encryptedText, char[] alphabet) {

        int bestScore = Integer.MIN_VALUE;
        String bestText = null;

        Cipher bruteCipher = new Cipher(alphabet);

        for (int i = 1; i < alphabet.length; i++) {
            try {
                String text = bruteCipher.decrypt(encryptedText, i);
                int score = score(text);

                if(score > bestScore) {
                    bestScore = score;
                    bestText = text;
                }

            } catch (CipherException e) {
                // Ключ i не подошёл — продолжаем перебор.
            }
        }

        return bestText;

    }

    private int score (String text) {

        String[] words = text.split(" ");
        int score = 0;

        for (String value : words) {
            if (value.isEmpty()) {
                score -= 1;
                continue;
            }

            if (value.length() >= 15) {
                score -= 2;
            }

            if (value.length() <= 2 && COMMON.contains(value)) {
                score += 3;
            }
        }

        for (int i = 0; i < text.length() - 1; i++) {
            char c = text.charAt(i);
            if (c == '.' || c == ',' || c == '!' || c == '?' || c == ':' || c == '»') {
                score += (text.charAt(i + 1) == ' ') ? 2 : -2;
            }
        }

        long spaces = text.chars().filter(ch -> ch == ' ').count();
        if (!text.isEmpty() && spaces * 100 / text.length() >= 10) score += 5;

        return score;

    }


}
