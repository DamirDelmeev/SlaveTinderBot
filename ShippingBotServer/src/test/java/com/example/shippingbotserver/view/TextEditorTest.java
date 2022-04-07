package com.example.shippingbotserver.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextEditorTest {

    @Test
    void textEditorGetTitle() {
        TextEditor textEditor = new TextEditor();
        assertEquals("All", textEditor.getTitle("All okay"));

        assertEquals("Allllllllllllllll...", textEditor.getTitle("Allllllllllllllllfvebr okay"));
    }

    @Test
    void textEditorGetText() {
        TextEditor textEditor = new TextEditor();
        assertEquals("okay", textEditor.getText("All. okay"));

        assertEquals("okay", textEditor.getText("Allllllllllllllllfvebr okay"));
    }

    @Test
    void sizeOfString() {
        TextEditor textEditor = new TextEditor();
        assertEquals(75, textEditor.sizeOfString(12));
        assertEquals(30, textEditor.sizeOfString(29));
        assertEquals(48, textEditor.sizeOfString(75));
        assertEquals(900, textEditor.sizeOfString(1));
        assertEquals(51, textEditor.sizeOfString(113));
    }

    @Test
    void sizeOfFont() {
        TextEditor textEditor = new TextEditor();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 450; i++) {
            stringBuilder.append('1');
        }
        assertEquals(28, textEditor.sizeOfFont(stringBuilder.toString()));
        for (int i = 0; i < 45; i++) {
            stringBuilder.append('1');
        }
        assertEquals(25, textEditor.sizeOfFont(stringBuilder.toString()));
        for (int i = 0; i < 100; i++) {
            stringBuilder.append('1');
        }
        assertEquals(19, textEditor.sizeOfFont(stringBuilder.toString()));
        assertEquals(28, textEditor.sizeOfFont("My friend is my cat"));
    }
}