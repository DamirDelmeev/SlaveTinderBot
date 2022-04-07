package com.example.shippingbotserver.translation;

import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextHandlerTest {

    @Test
    void getTranslation() {
        TextHandler textHandler = new TextHandler();
        assertEquals("Привѣтъ", textHandler.getTranslation("Привет"));
        assertEquals("Привѣтъ, Антонъ", textHandler.getTranslation("Привет, Антон"));
        assertEquals("", textHandler.getTranslation(""));
        assertEquals("Пришелъ герой за огурцами",
                textHandler.getTranslation("Пришел герой за огурцами"));
    }

    @Test
    void loadNameList() throws IOException {
        TextHandler textHandler = new TextHandler();
        List<String> names = new ArrayList<>();
        try (FileReader fileReader = new FileReader("src/main/resources/ListName.txt")) {
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNext()) {
                names.add(scanner.nextLine());
            }
        }
        assertEquals(names, textHandler.loadNameList());
    }
}