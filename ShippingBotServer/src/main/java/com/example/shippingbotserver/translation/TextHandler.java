package com.example.shippingbotserver.translation;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс реализует перевод.
 */
@Component
public class TextHandler {
    private final List<Character> vowels = Arrays.asList('а', 'о', 'и', 'е', 'ё', 'э', 'ы', 'у', 'ю', 'я', 'й', 'ь');

    /**
     * Метод реализует перевод
     *
     * @param text -текст без перевода
     * @return текст перевода
     */
    public String getTranslation(String text) {
        String[] wordsBeforeChange = text.split("[ \n]");
        List<String> wordsAfterChange = new ArrayList<>();
        List<String> listName = loadNameList();
        listName = listName.stream().map(name -> name = name.toLowerCase(Locale.ROOT)).collect(Collectors.toList());
        for (String s : wordsBeforeChange) {
            String wordWithoutSymbols = s.replaceAll("[^а-яёА-ЯЁ-]", "");
            if (!wordWithoutSymbols.isEmpty()) {
                String wordFI = geFi(wordWithoutSymbols, listName);
                String wordEt = getEt(wordFI);
                String wordEr = getErSymbol(wordEt);
                String wordDecimalI = geDecimalI(wordEr);
                wordsAfterChange.add(wordDecimalI);
            }
        }
        for (int i = 0; i < wordsBeforeChange.length; i++) {
            if (wordsBeforeChange[i].equals(""))
                wordsAfterChange.add(i, " ");
        }
        List<String> results = getResulLine(wordsBeforeChange, wordsAfterChange);
        return String.join(" ", results);
    }

    public List<String> loadNameList() {
        List<String> list = new ArrayList<>();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classloader.getResourceAsStream("ListName.txt");
            InputStreamReader streamReader =
                    new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            for (String line; (line = reader.readLine()) != null; ) {
                list.add(line);
            }
        } catch (IOException E) {
            throw new RuntimeException("Ошибка при поиске файла с именами");
        }
        return list;
    }

    /**
     * Метод сравнивает два списка слов на наличие знаков препинания
     *
     * @param words,listAfterChange -слова без перевода и слова с переводом
     * @return список слов со знаками препинания и переводом
     */
    private List<String> getResulLine(String[] words, List<String> listAfterChange) {
        List<String> results = new ArrayList<>();
        List<String> wordsBeforeChange = Arrays.asList(words);
        for (int i = 0; i < wordsBeforeChange.size(); i++) {
            String wordBefore = wordsBeforeChange.get(i);
            String wordAfter = listAfterChange.get(i);
            int lengthWordAfter = wordAfter.length() - getErCounter(wordAfter);
            if (wordBefore.length() > (lengthWordAfter)) {
                addMark(results, wordBefore, wordAfter);
            } else {
                results.add(wordAfter);
            }
        }
        return results;
    }

    /**
     * Метод добавляет знаки препинания
     *
     * @param results,wordBefore,wordAfter -результат, слова без перевода и слова с переводом
     */
    private void addMark(List<String> results, String wordBefore, String wordAfter) {
        StringBuilder stringBuilder = new StringBuilder(wordAfter);
        for (int j = 0; j < wordBefore.length(); j++) {
            if (!Character.isLetter(wordBefore.charAt(j)) & (wordBefore.charAt(j) != '-' | wordBefore.length() == 1)) {
                stringBuilder.insert(j == 0 ? 0 : stringBuilder.length(), wordBefore.charAt(j));
            }
        }
        results.add(stringBuilder.toString());
    }

    /**
     * Метод определяет количество символов ъ-"ер" для реализации пунктуации
     *
     * @param wordAfter -слова с переводом
     */
    private int getErCounter(String wordAfter) {
        int counterEr = 0;
        if (wordAfter.contains("ъ-")) {
            counterEr++;
        }
        if (wordAfter.length() > 1 && wordAfter.charAt(wordAfter.length() - 1) == 'ъ') {
            counterEr++;
        }
        return counterEr;
    }

    /**
     * Метод заменяет в словах е символом Ѣ (Ять) согласно правилам
     * Если в современном русском слове встречается подстрока из списка listForEt
     *
     * @param wordDecimalI -слово в котором уже поменялось часть символов
     * @return wordDecimalI-слов с символом  Ѣ (Ять)
     */
    private String getEt(String wordDecimalI) {
        List<String> listWithRoot = loadRootList();
        for (String root : listWithRoot) {
            String stringBuilder = changeSymbolsInRoot(wordDecimalI, root);
            if (stringBuilder != null) return stringBuilder;
        }
        return wordDecimalI;
    }

    private List<String> loadRootList() {
        List<String> list = new ArrayList<>();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classloader.getResourceAsStream("ListRoot.txt");
            InputStreamReader streamReader =
                    new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            for (String line; (line = reader.readLine()) != null; ) {
                list.add(line);
            }
        } catch (IOException E) {
            throw new RuntimeException("Ошибка при поиске файла с именами");
        }
        return list;
    }

    /**
     * Метод реализует замену исключительно в корнях слова согласно правилам
     *
     * @param wordDecimalI,root -слово в котором уже поменялось часть символов и корень
     * @return wordDecimalI-слов с символом Ѣ (Ять)
     */
    private String changeSymbolsInRoot(String wordDecimalI, String root) {
        if (wordDecimalI.toLowerCase().contains(root)) {
            int begin = wordDecimalI.toLowerCase().indexOf(root);

            StringBuilder stringBuilder = new StringBuilder(wordDecimalI);
            for (int i = 0; i < root.length(); i++) {
                if (wordDecimalI.charAt(begin + i) == 'е') {
                    stringBuilder.setCharAt(begin + i, 'ѣ');
                }
                if (wordDecimalI.charAt(begin + i) == 'Е') {
                    stringBuilder.setCharAt(begin + i, 'Ѣ');
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }

    /**
     * Метод заменяет в словах е символом ѳ (фита) согласно правилам
     * Употребляется вместо нынешнего ф в именах:
     *
     * @param wordWithoutSymbols -слово в котором уже поменялось часть символов listName-список имён с буквой ф
     * @return wordDecimalI-слово с символом ѳ (фита)
     */
    private String geFi(String wordWithoutSymbols, List<String> listName) {
        StringBuilder stringBuilder = new StringBuilder(wordWithoutSymbols);

        if (listName.contains(wordWithoutSymbols.toLowerCase(Locale.ROOT))) {
            if (wordWithoutSymbols.charAt(0) == 'ф' | wordWithoutSymbols.charAt(0) == 'Ф') {
                stringBuilder.setCharAt(0, 'Ѳ');
            } else {
                stringBuilder.setCharAt(0, Character.toUpperCase(wordWithoutSymbols.charAt(0)));
                return stringBuilder.toString().replace('ф', 'ѳ');
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Метод заменяет в словах е символом i («и десятеричное») согласно правилам Его нужно писать на месте нынешнего
     * и, если сразу после него идет другая гласная буква
     *
     * @param wordEr -слово в котором уже поменялось часть символов
     * @return wordDecimalI-слово с символом i («и десятеричное»)
     */
    private String geDecimalI(String wordEr) {
        for (int i = 0; i < wordEr.length(); i++) {
            if ((wordEr.charAt(i)) == 'и' & wordEr.length() > i + 1 && vowels.contains(wordEr.charAt(i + 1))) {
                StringBuilder stringBuilder = new StringBuilder(wordEr);
                stringBuilder.setCharAt(i, 'i');
                return stringBuilder.toString();
            }
        }
        return wordEr;
    }

    /**
     * Метод заменяет в словах е символом ъ-"ер" согласно правилам в конце всякого слова, оканчивающегося на согласную.
     * Исключение — слова, оканчивающиеся на й;
     *
     * @param word -слово в котором уже поменялось часть символов
     * @return wordDecimalI-слово с символом  ъ-"ер"
     */
    private String getErSymbol(String word) {
        if (!word.contains("-") & word.length() > 1) {
            if (!vowels.contains(Character.toLowerCase(word.charAt(word.length() - 1)))) {
                word = word + "ъ";
            }
        } else {
            if (word.length() == 1) {
                return word;
            }
            String[] split = word.split("-");
            return Arrays.stream(split).map(this::getErSymbol).collect(Collectors.joining("-"));
        }
        return word;
    }


//    public static void main(String[] args) {
//        try {
//            TextHandler textHandler = new TextHandler();
//            System.out.println(textHandler.getTranslation("Я"));
////            System.out.println(textHandler.getTranslation("Санкт-Петербург - это древний и очень красивый город нашей " +
////                    "страны России. Он является вторым по величине после Москвы, это важнейший центр туризма, экономики, медицины, науки, культуры нашего государства. Этот город имеет очень богатое историческое наследие для всего человечества нашей планеты.\n" +
////                    "Источник: Сочинение на тему Санкт-Петербург, России, Фусик, федот, Глафира, софья, беглец Еда"));
////            System.out.println(textHandler.getTranslation("\"Санкт-Петербург\" - это древний, очень красивый город " +
////                    "нашей"));
////            System.out.println(textHandler.getTranslation(
////                    "Привет, я помогу тебе найти кого-нибудь ,чтобы скрасить твоё одиночество."));
//        }catch (RuntimeException e){
//            throw  new RuntimeException("Пользователь ввёл текст с ошибками");
//        }
//    }
}
