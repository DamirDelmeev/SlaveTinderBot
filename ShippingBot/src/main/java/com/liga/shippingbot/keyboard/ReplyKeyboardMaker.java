package com.liga.shippingbot.keyboard;

import com.liga.shippingbot.constants.ConstantsName;
import org.springframework.stereotype.Component;
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

    public ReplyKeyboardMarkup getKeyboard(List<ConstantsName> namesButton) {
        KeyboardRow row = new KeyboardRow();
        namesButton.forEach(constantsName -> row.add(new KeyboardButton(constantsName.getName())));
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}
