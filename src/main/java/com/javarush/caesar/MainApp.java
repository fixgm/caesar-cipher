package com.javarush.caesar;

import com.javarush.caesar.core.BruteForce;
import com.javarush.caesar.core.Cipher;
import com.javarush.caesar.core.StatisticalAnalyzer;
import com.javarush.caesar.exception.CipherException;
import com.javarush.caesar.service.FileManager;
import com.javarush.caesar.service.Validator;

import java.util.Locale;
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
        System.out.println("4. Взломать статический анализ");
        System.out.println("0. Выход (или exit)");
    }

    private void encryptFile() throws CipherException {

        String in = ask("Исходный файл: ");
        String out = ask("Файл с результатом: ");
        String key = ask("Ключ: ");

        if (!key.matches("\\d+")) {
            throw new CipherException("В качестве ключа, может использоваться только целое число");
        }

        if (!validator.isFileExists(in)) {
            throw new CipherException("Исходный файл: '" + in + "' невалиден или такого файла не существует");
        }
        if (!validator.isFileExists(out)) {
            throw new CipherException("Файл с результатом: '" + out + "' невалиден или такого файла не существует");
        }

        if (!validator.isValidKey(Integer.parseInt(key), ALPHABET)) {
            throw new CipherException("Ключ имеет недопустимую длину. Допустимые значения: 0 - " + (ALPHABET.length - 1));
        }

        String content = fileManager.readFile(in);

        content = cipher.encrypt(content.toLowerCase(Locale.ROOT), Integer.parseInt(key));

        fileManager.writeFile(content, out);

        System.out.println("Файл зашифрован: '" + out + "'");
    }

    private void decryptFile() throws CipherException {

        String in = ask("Исходный файл: ");
        String out = ask("Файл с результатом: ");
        String key = ask("Ключ: ");

        if (!key.matches("\\d+")) {
            throw new CipherException("В качестве ключа, может использоваться только целое число");
        }

        if (!validator.isFileExists(in)) {
            throw new CipherException("Исходный файл: '" + in + "' невалиден или такого файла не существует");
        }
        if (!validator.isFileExists(out)) {
            throw new CipherException("Файл с результатом: '" + out + "' невалиден или такого файла не существует");
        }

        if (!validator.isValidKey(Integer.parseInt(key), ALPHABET)) {
            throw new CipherException("Ключ имеет недопустимую длину. Допустимые значения: 0 - " + (ALPHABET.length - 1));
        }

        String content = fileManager.readFile(in);

        content = cipher.decrypt(content.toLowerCase(Locale.ROOT), Integer.parseInt(key));

        fileManager.writeFile(content, out);

        System.out.println("Файл расшифрован: '" + out + "'");
    }

    private void bruteForceFile() throws CipherException {
        String in = ask("Исходный файл: ");
        String out = ask("Файл с результатом: ");

        if (!validator.isFileExists(in)) {
            throw new CipherException("Исходный файл: '" + in + "' невалиден или такого файла не существует");
        }
        if (!validator.isFileExists(out)) {
            throw new CipherException("Файл с результатом: '" + out + "' невалиден или такого файла не существует");
        }

        String content = fileManager.readFile(in);
        content = bruteForce.decryptByBruteForce(content, ALPHABET);
        if (content == null) {
            throw new CipherException("Не удалось подобрать ключ");
        }

        fileManager.writeFile(content, out);

        System.out.println("Файл взломан: '" + out + "'");

    }

    private void analyzerFile() throws CipherException {
        String in = ask("Исходный файл: ");
        String out = ask("Файл с результатом: ");

        if (!validator.isFileExists(in)) {
            throw new CipherException("Исходный файл: '" + in + "' невалиден или такого файла не существует");
        }
        if (!validator.isFileExists(out)) {
            throw new CipherException("Файл с результатом: '" + out + "' невалиден или такого файла не существует");
        }

        String content = fileManager.readFile(in);
        content = statisticalAnalyzer.decryptByAnalyze(content, ALPHABET);

        if (content == null) {
            throw new CipherException("Не удалось подобрать ключ");
        }

        fileManager.writeFile(content, out);

        System.out.println("Файл взломан: '" + out + "'");

    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

}
