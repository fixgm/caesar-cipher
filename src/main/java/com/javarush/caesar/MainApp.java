package com.javarush.caesar;

import com.javarush.caesar.core.BruteForce;
import com.javarush.caesar.core.Cipher;
import com.javarush.caesar.core.StatisticalAnalyzer;
import com.javarush.caesar.exception.CipherException;
import com.javarush.caesar.service.FileManager;
import com.javarush.caesar.service.Validator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class MainApp {

    private static final char[] ALPHABET = {
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п',
            'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
            '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '
    };

    private final Scanner scanner = new Scanner(System.in);
    private final FileManager fileManager = new FileManager();
    private final Validator validator = new Validator();
    private final Cipher cipher = new Cipher(ALPHABET);
    private final BruteForce bruteForce = new BruteForce();
    private final StatisticalAnalyzer statisticalAnalyzer = new StatisticalAnalyzer();

    public static void main(String[] args) {
        new MainApp().run();
    }

    private void run() {
        System.out.println("Шифр Цезаря: шифрование и расшифровка файлов");
        while (true) {
            printMenu();
            String choice = ask("Выберите действие: ");
            try {
                switch (choice) {
                    case "1":
                        encryptFile();
                        break;
                    case "2":
                        decryptFile();
                        break;
                    case "3":
                        bruteForceFile();
                        break;
                    case "4":
                        analyzerFile();
                        break;
                    case "0":
                    case "exit":
                        System.out.println("До свидания!");
                        return;
                    default:
                        System.out.println("Нет такого пункта: " + choice);
                }
            } catch (CipherException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Зашифровать файл");
        System.out.println("2. Расшифровать файл с ключом");
        System.out.println("3. Взломать перебором ключей");
        System.out.println("4. Взломать статистический анализ");
        System.out.println("0. Выход (или exit)");
    }

    private void encryptFile() throws CipherException {

        Map<String, String> parameters = readInputParameters(3);

        String content = fileManager.readFile(parameters.get("in"));

        content = cipher.encrypt(content.toLowerCase(Locale.ROOT), Integer.parseInt(parameters.get("key")));

        fileManager.writeFile(content, parameters.get("out"));

        System.out.println("Файл зашифрован: '" + parameters.get("out") + "'");
    }

    private void decryptFile() throws CipherException {

        Map<String, String> parameters = readInputParameters(3);

        String content = fileManager.readFile(parameters.get("in"));

        content = cipher.decrypt(content.toLowerCase(Locale.ROOT), Integer.parseInt(parameters.get("key")));

        fileManager.writeFile(content, parameters.get("out"));

        System.out.println("Файл расшифрован: '" + parameters.get("out") + "'");
    }

    private void bruteForceFile() throws CipherException {

        Map<String, String> parameters = readInputParameters(2);

        String content = fileManager.readFile(parameters.get("in"));
        content = bruteForce.decryptByBruteForce(content, ALPHABET);
        if (content == null) {
            throw new CipherException("Не удалось подобрать ключ");
        }

        fileManager.writeFile(content, parameters.get("out"));

        System.out.println("Файл взломан: '" + parameters.get("out") + "'");

    }

    private void analyzerFile() throws CipherException {

        Map<String, String> parameters = readInputParameters(2);

        String content = fileManager.readFile(parameters.get("in"));
        content = statisticalAnalyzer.decryptByAnalyze(content, ALPHABET);

        if (content == null) {
            throw new CipherException("Не удалось подобрать ключ");
        }

        fileManager.writeFile(content, parameters.get("out"));

        System.out.println("Файл взломан: '" + parameters.get("out") + "'");

    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private Map<String, String> readInputParameters(int paramsCount) throws CipherException {

        if (paramsCount < 2 || paramsCount > 3) {
            throw new CipherException("Количество параметров может быть от 2 до 3");
        }

        Map<String, String> params = new HashMap<>();

        params.put("in", ask("Исходный файл: "));
        params.put("out", ask("Файл с результатом: "));

        //params.get("in")

        if (!validator.isFileExists(params.get("in"))) {
            throw new CipherException("Исходный файл: '" + params.get("in") + "' невалиден или такого файла не существует");
        }


        if (validator.isFileExists(params.get("out"))) {
            throw new CipherException("Файл с результатом: '" + params.get("out") + "' невозможно записать в существующий файл");
        }


        if (paramsCount == 3) {
            params.put("key", ask("Ключ: "));

            if (!params.get("key").matches("\\d+")) {
                throw new CipherException("В качестве ключа, может использоваться только целое число");
            }

            if (!validator.isValidKey(Integer.parseInt(params.get("key")), ALPHABET)) {
                throw new CipherException("Ключ имеет недопустимую длину. Допустимые значения: 0 - " + (ALPHABET.length - 1));
            }

        }

        return params;

    }


}
