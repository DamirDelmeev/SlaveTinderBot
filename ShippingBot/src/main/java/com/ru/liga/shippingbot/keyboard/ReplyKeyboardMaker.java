package com.ru.liga.shippingbot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализует инициализацию объектов для меню с кнопками.
 */
@Component
public class ReplyKeyboardMaker {
    public ReplyKeyboardMarkup getKeyboardFirstStageForForm() {
        KeyboardRow rowFirst = new KeyboardRow();
        rowFirst.add(new KeyboardButton("Сударь"));
        rowFirst.add(new KeyboardButton("Сударыня"));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(rowFirst);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboard getKeyboardSecondStageForForm() {
        KeyboardRow rowFirst = new KeyboardRow();
        rowFirst.add(new KeyboardButton("Сударя"));
        rowFirst.add(new KeyboardButton("Сударыню"));
        rowFirst.add(new KeyboardButton("Всех"));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(rowFirst);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboard getMenu(String firstButton, String secondButton, String thirdButton) {
        KeyboardRow rowFirst = new KeyboardRow();
        rowFirst.add(new KeyboardButton(firstButton));

        KeyboardRow rowSecond = new KeyboardRow();
        rowSecond.add(new KeyboardButton(secondButton));
        rowFirst.add(new KeyboardButton(thirdButton));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(rowFirst);
        keyboard.add(rowSecond);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboard getMenu(String firstButton, String secondButton, String thirdButton, String fourthButton,
                                 String menuButton) {
        KeyboardRow rowFirst = new KeyboardRow();
        rowFirst.add(new KeyboardButton(firstButton));
        rowFirst.add(new KeyboardButton(secondButton));

        KeyboardRow rowSecond = new KeyboardRow();
        rowSecond.add(new KeyboardButton(thirdButton));
        rowFirst.add(new KeyboardButton(fourthButton));
        rowFirst.add(new KeyboardButton(menuButton));
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(rowFirst);
        keyboard.add(rowSecond);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }
}
