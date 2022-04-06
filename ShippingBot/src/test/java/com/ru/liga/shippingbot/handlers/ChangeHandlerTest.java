package com.ru.liga.shippingbot.handlers;

import com.ru.liga.shippingbot.bot.BotState;
import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChangeHandlerTest {

    @Test
    void getChangeFormFirstStageTestBotStateAndReturn() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        ChangeHandler changeHandler = new ChangeHandler();
        changeHandler.setMap(map);
        changeHandler.getChangeFormFirstStage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Укажите поле, которое хотите изменить.");
        sendMessage.setChatId("1");
        BotApiMethod<?> botApiMethod = sendMessage;
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getMenu
                ("Пол", "Имя", "Описание", "Приоритет поиска"));

        assertEquals(botApiMethod, changeHandler.getChangeFormFirstStage(1L));
        assertEquals(BotState.SHOW_CHANGES, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> changeHandler.getChangeFormFirstStage(2L));
    }

    @Test
    void getChangeGenderSecondStageTestBotStateAndReturn() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        ChangeHandler changeHandler = new ChangeHandler();
        changeHandler.setMap(map);
        changeHandler.getChangeGenderSecondStage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Вы сударь иль сударыня?");
        sendMessage.setChatId("1");
        BotApiMethod<?> botApiMethod = sendMessage;
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboardFirstStageForForm());

        assertEquals(botApiMethod, changeHandler.getChangeGenderSecondStage(1L));
        assertEquals(BotState.CHANGE_GENDER, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> changeHandler.getChangeGenderSecondStage(2L));
    }

    @Test
    void getChangeNameSecondStageTestBotStateAndReturn() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        ChangeHandler changeHandler = new ChangeHandler();
        changeHandler.setMap(map);
        changeHandler.getChangeNameSecondStage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Как вас зовут?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        BotApiMethod<?> botApiMethod = sendMessage;

        assertEquals(botApiMethod, changeHandler.getChangeNameSecondStage(1L));
        assertEquals(BotState.CHANGE_NAME, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> changeHandler.getChangeNameSecondStage(2L));
    }

    @Test
    void getChangeDescriptionSecondStageTestBotStateAndReturn() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        ChangeHandler changeHandler = new ChangeHandler();
        changeHandler.setMap(map);
        changeHandler.getChangeDescriptionSecondStage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Опишите себя.");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        BotApiMethod<?> botApiMethod = sendMessage;

        assertEquals(botApiMethod, changeHandler.getChangeDescriptionSecondStage(1L));
        assertEquals(BotState.CHANGE_DESCRIPTION, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> changeHandler.getChangeDescriptionSecondStage(2L));
    }

    @Test
    void getChangePreferenceSecondStage() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        ChangeHandler changeHandler = new ChangeHandler();
        changeHandler.setMap(map);
        changeHandler.getChangePreferenceSecondStage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Кого вы ищите?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboardSecondStageForForm());
        BotApiMethod<?> botApiMethod = sendMessage;

        assertEquals(botApiMethod, changeHandler.getChangePreferenceSecondStage(1L));
        assertEquals(BotState.CHANGE_PREFERENCE, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> changeHandler.getChangePreferenceSecondStage(2L));
    }

}