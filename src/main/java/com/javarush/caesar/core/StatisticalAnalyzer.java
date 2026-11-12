package com.javarush.caesar.core;

import com.javarush.caesar.exception.CipherException;

public class StatisticalAnalyzer {

    private static final double[] EXPECTED_FREQ = {
            80.1, 1.6, 4.5, 1.7, 3.0, 84.5, 0.04, 0.9, 1.6, 7.4, 1.2, 3.5, 4.4, 3.2, 6.7, 11.0,
            2.8, 4.7, 5.5, 6.3, 2.6, 0.3, 1.0, 0.5, 1.4, 0.7, 0.4, 0.04, 1.9, 1.7, 0.3, 0.6, 2.0,
            175.0 // <-- Частота пробела (примерно 17.5%)
    };

    public String decryptByAnalyze(String encryptedText, char[] alphabet) throws CipherException {
        if (encryptedText == null || encryptedText.isEmpty()) {
            throw new CipherException("Текст для анализа пуст");
        }

        Cipher analyzeCipher = new Cipher(alphabet);

        double[] encryptedFreq = getTextFrequencies(encryptedText, alphabet);

        double minDeviation = Double.MAX_VALUE;
        int bestKey = 0;


        for (int shift = 0; shift < alphabet.length; shift++) {
            double deviation = 0.0;


            for (int i = 0; i < alphabet.length; i++) {
                int expectedIndex = (i - shift + alphabet.length) % alphabet.length;


                double expected = 0.0;
                if (expectedIndex < EXPECTED_FREQ.length) {
                    expected = EXPECTED_FREQ[expectedIndex];
                } else if (alphabet[expectedIndex] == ' ') {
                    expected = 175.0;
                }

                double diff = encryptedFreq[i] - expected;
                deviation += diff * diff;
            }

            if (deviation < minDeviation) {
                minDeviation = deviation;
                bestKey = shift;
            }
        }

        return analyzeCipher.decrypt(encryptedText, bestKey);
    }

    private double[] getTextFrequencies(String text, char[] alphabet) {
        int[] counts = new int[alphabet.length];
        int totalChars = 0;

        for (char c : text.toLowerCase().toCharArray()) {
            int index = indexOf(alphabet, c);
            if (index != -1) {
                counts[index]++;
                totalChars++;
            }
        }

        double[] freq = new double[alphabet.length];
        if (totalChars == 0) return freq;

        for (int i = 0; i < alphabet.length; i++) {
            freq[i] = ((double) counts[i] / totalChars) * 1000.0;
        }
        return freq;
    }

    private int indexOf(char[] alphabet, char c) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == c) return i;
        }
        return -1;
    }
}