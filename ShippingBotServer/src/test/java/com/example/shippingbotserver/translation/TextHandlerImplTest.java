package com.example.shippingbotserver.translation;

import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextHandlerImplTest {

    @Test
    void getTranslation() {
        TextHandlerImpl textHandlerImpl = new TextHandlerImpl();
        assertEquals("Привѣтъ", textHandlerImpl.getTranslation("Привет"));
        assertEquals("Привѣтъ, Антонъ", textHandlerImpl.getTranslation("Привет, Антон"));
        assertEquals("", textHandlerImpl.getTranslation(""));
        assertEquals("Пришелъ герой за огурцами",
                textHandlerImpl.getTranslation("Пришел герой за огурцами"));
    }

    @Test
    void loadNameList() throws IOException {
        TextHandlerImpl textHandlerImpl = new TextHandlerImpl();
        List<String> names = new ArrayList<>();
        try (FileReader fileReader = new FileReader("src/main/resources/ListName.txt")) {
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNext()) {
                names.add(scanner.nextLine());
            }
        }
        assertEquals(names, textHandlerImpl.loadList("ListName.txt"));
    }
}