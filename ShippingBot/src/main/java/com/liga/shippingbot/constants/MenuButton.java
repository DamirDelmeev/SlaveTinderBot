package com.liga.shippingbot.constants;

import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;

public enum MenuButton implements ConstantsName {
    SEARCH_BUTTON("Поиск"),
    FAVORITE_BUTTON("Любимцы"),
    MENU_BUTTON("Меню"),
    FORM_BUTTON("Анкета"),
    CHANGE_BUTTON("Изменить анкету"),
    CHANGE_GENDER_BUTTON("Пол"),
    CHANGE_NAME_BUTTON("Имя"),
    CHANGE_PREFERENCE_BUTTON("Приоритет поиска"),
    CHANGE_DESCRIPTION_BUTTON("Описание"),
    LIKE_BUTTON(EmojiParser.parseToUnicode(":hearts:")),
    DISLIKE_BUTTON(EmojiParser.parseToUnicode(":heavy_multiplication_x:")),
    FAVORITE_LЕFT_BUTTON("Влево"),
    FAVORITE_RIGHT_BUTTON("Вправо");
    @Getter
    final String name;

    MenuButton(String name) {
        this.name = name;
    }
}
