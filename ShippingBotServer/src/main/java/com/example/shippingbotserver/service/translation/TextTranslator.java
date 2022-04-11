package com.example.shippingbotserver.service.translation;

import com.example.shippingbotserver.entity.User;
import com.example.shippingbotserver.view.TextEditor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс реализует перевод.
 */

@Component
@RequiredArgsConstructor
public class TextTranslator implements TextHandler {

    private final TextEditor textEditor;

    public void userTranslate(User user) {
        user.setGender(user.getGender().equals("boy") ? "Сударь" : "Сударыня");

        user.setName(user.getName().equals("") ? "" : (user.getName().matches("[^a-zA-Z]*") ?
                getTranslation(user.getName()) : user.getName()));

        user.setDescription(user.getDescription().matches("[^a-zA-Z]*") ?
                getTranslation(user.getDescription()) : user.getDescription());
    }

    public String getTranslation(String text) {
        if (text.isEmpty()) {
            return "";
        }
        List<String> listName = loadList("ListName.txt");
        listName = listName
                .stream()
                .map(name -> name = name.toLowerCase(Locale.ROOT))
                .collect(Collectors.toList());
        String patternString = "[а-яёА-ЯЁ]+";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);
        List<String> listWords = new ArrayList<>();
        while (matcher.find()) {
            listWords.add(matcher.group());
        }
        Map<String, String> replaceMap = new LinkedHashMap<>();
        List<String> finalListName = listName;
        listWords.forEach(word -> {
            String wordFI = getFi(word, finalListName);
            String wordEt = getEt(wordFI);
            String wordEr = getErSymbol(wordEt);
            String wordDecimalI = getDecimalI(wordEr);
            replaceMap.put(word, wordDecimalI);
        });
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            text = text.replaceAll(entry.getKey(), entry.getValue());
        }
        return text;
    }

    public List<String> loadList(String nameList) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(classloader.getResourceAsStream(nameList))
                        , StandardCharsets.UTF_8))) {
            List<String> list = new ArrayList<>();
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                list.add(line);
            }
            return list;
        } catch (IOException E) {
            throw new RuntimeException("Ошибка при поиске файла с именами");
        }
    }


    private String getEt(String wordDecimalI) {
        List<String> listWithRoot = loadList("ListRoot.txt");
        for (String root : listWithRoot) {
            String stringBuilder = changeSymbolsInRoot(wordDecimalI, root);
            if (stringBuilder != null) return stringBuilder;
        }
        return wordDecimalI;
    }


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

    private String getFi(String wordWithoutSymbols, List<String> listName) {
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


    private String getDecimalI(String wordEr) {
        List<String> vowels = loadList("ListVowels.txt");
        for (int i = 0; i < wordEr.length(); i++) {
            if ((wordEr.charAt(i)) == 'и' & wordEr.length() > i + 1 && vowels.contains(wordEr.charAt(i + 1))) {
                StringBuilder stringBuilder = new StringBuilder(wordEr);
                stringBuilder.setCharAt(i, 'i');
                return stringBuilder.toString();
            }
        }
        return wordEr;
    }

    private String getErSymbol(String word) {
        if (!word.contains("-") & word.length() > 1) {
            List<String> vowels = loadList("ListVowels.txt");
            char lastChar = Character.toLowerCase(word.charAt(word.length() - 1));
            if (!vowels.contains(Character.toString(lastChar))) {
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
}
