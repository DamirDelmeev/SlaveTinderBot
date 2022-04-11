package com.example.shippingbotserver.service.translation;

/**
 * Класс реализующий этот интерфейс производит перевод.
 */
public interface TextHandler {
    /**
     * Метод реализует перевод
     *
     * @param text -текст без перевода
     * @return текст перевода
     */
    String getTranslation(String text);

    /**
     * Метод загружает файл в лист
     *
     * @param nameList -имя файл с данными
     * @return данные файла в листе
     */
    // List<String> loadList(String nameList);

    /**
     * Метод заменяет в словах е символом Ѣ (Ять) согласно правилам
     * Если в современном русском слове встречается подстрока из списка listForEt
     *
     * @param wordDecimalI -слово в котором уже поменялось часть символов
     * @return wordDecimalI-слов с символом  Ѣ (Ять)
     */
    //  String getEt(String wordDecimalI) ;

    /**
     * Метод реализует замену исключительно в корнях слова согласно правилам
     *
     * @param wordDecimalI,root -слово в котором уже поменялось часть символов и корень
     * @return wordDecimalI-слов с символом Ѣ (Ять)
     */
    //  String changeSymbolsInRoot(String wordDecimalI, String root) ;

    /**
     * Метод заменяет в словах е символом ѳ (фита) согласно правилам
     * Употребляется вместо нынешнего ф в именах:
     *
     * @param wordWithoutSymbols -слово в котором уже поменялось часть символов listName-список имён с буквой ф
     * @return wordDecimalI-слово с символом ѳ (фита)
     */
    //String getFi(String wordWithoutSymbols, List<String> listName) ;

    /**
     * Метод заменяет в словах е символом i («и десятеричное») согласно правилам Его нужно писать на месте нынешнего
     * и, если сразу после него идет другая гласная буква
     *
     * @param wordEr -слово в котором уже поменялось часть символов
     * @return wordDecimalI-слово с символом i («и десятеричное»)
     */
    // String getDecimalI(String wordEr);

    /**
     * Метод заменяет в словах е символом ъ-"ер" согласно правилам в конце всякого слова, оканчивающегося на согласную.
     * Исключение — слова, оканчивающиеся на й;
     *
     * @param word -слово в котором уже поменялось часть символов
     * @return wordDecimalI-слово с символом  ъ-"ер"
     */
    //  String getErSymbol(String word);

}
