package com.ru.liga.shippingbot.handlers;

import com.ru.liga.shippingbot.bot.BotState;
import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.handlers.template.RequestRunner;
import com.ru.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChangeHandlerTest {
    @Mock
    RequestRunner requestRunner;

    ChangeHandler changeHandler;

    public ChangeHandlerTest() {
        MockitoAnnotations.openMocks(this);
        this.changeHandler = new ChangeHandler(requestRunner);
    }

    @Test
    void getChangeFormFirstStageTestBotStateAndReturn() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        changeHandler.setMap(map);
        changeHandler.getChangeFormFirstStage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Укажите поле, которое хотите изменить.");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getMenu
                ("Пол", "Имя", "Меню", "Приоритет поиска", "Описание"));

        assertEquals(sendMessage, changeHandler.getChangeFormFirstStage(1L));
        assertEquals(BotState.SHOW_CHANGES, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> changeHandler.getChangeFormFirstStage(2L));
    }

    @Test
    void getChangeGenderSecondStageTestBotStateAndReturn() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());

        changeHandler.setMap(map);
        changeHandler.getChangeGenderSecondStage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Вы сударь иль сударыня?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboardFirstStageForForm());

        assertEquals(sendMessage, changeHandler.getChangeGenderSecondStage(1L));
        assertEquals(BotState.CHANGE_GENDER, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> changeHandler.getChangeGenderSecondStage(2L));
    }

    @Test
    void getChangeNameSecondStageTestBotStateAndReturn() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        changeHandler.setMap(map);
        changeHandler.getChangeNameSecondStage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Как вас зовут?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

        assertEquals(sendMessage, changeHandler.getChangeNameSecondStage(1L));
        assertEquals(BotState.CHANGE_NAME, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> changeHandler.getChangeNameSecondStage(2L));
    }

    @Test
    void getChangeDescriptionSecondStageTestBotStateAndReturn() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        changeHandler.setMap(map);
        changeHandler.getChangeDescriptionSecondStage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Опишите себя.");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

        assertEquals(sendMessage, changeHandler.getChangeDescriptionSecondStage(1L));
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

        assertEquals(sendMessage, changeHandler.getChangePreferenceSecondStage(1L));
        assertEquals(BotState.CHANGE_PREFERENCE, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> changeHandler.getChangePreferenceSecondStage(2L));
    }

    @Test
    void addChangeGenderTest() {
        Message message = new Message();
        message.setText("ля ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        changeHandler.setMap(map);

        assertThrows(RuntimeException.class, () -> changeHandler.addChangeGender(message, 1L));

        message.setText("Сударь");
        SendMessage sendMessage = new SendMessage("1", "Вы успешно внесли изменение.");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getMenu("Анкета", "Поиск", "Любимцы"));

        assertEquals(sendMessage, changeHandler.addChangeGender(message, 1L));
        assertEquals(BotState.SHOW_MAIN_MENU, map.get(1L).getBotState());
        assertEquals("Сударь", map.get(1L).getGender());
    }

    @Test
    void addChangeNameTest() {
        Message message = new Message();
        message.setText("ля ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        changeHandler.setMap(map);

        SendMessage sendMessage = new SendMessage("1", "Вы успешно внесли изменение.");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker()
                .getMenu("Анкета", "Поиск", "Любимцы"));

        assertEquals(sendMessage, changeHandler.addChangeName(message, 1L));
        assertEquals(BotState.SHOW_MAIN_MENU, map.get(1L).getBotState());
        assertEquals("ля ля", map.get(1L).getName());
    }

    @Test
    void addChangeDescription() {
        Message message = new Message();
        message.setText("ля ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        changeHandler.setMap(map);

        SendMessage sendMessage = new SendMessage("1", "Вы успешно внесли изменение.");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker()
                .getMenu("Анкета", "Поиск", "Любимцы"));

        assertEquals(sendMessage, changeHandler.addChangeDescription(message, 1L));
        assertEquals(BotState.SHOW_MAIN_MENU, map.get(1L).getBotState());
        assertEquals("ля ля", map.get(1L).getDescription());
    }

    @Test
    void addChangePreference() {
        Message message = new Message();
        message.setText("ля ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        changeHandler.setMap(map);

        assertThrows(RuntimeException.class, () -> changeHandler.addChangePreference(message, 1L));

        message.setText("Сударя");
        SendMessage sendMessage = new SendMessage("1", "Вы успешно внесли изменение.");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getMenu("Анкета", "Поиск", "Любимцы"));


        assertEquals(sendMessage, changeHandler.addChangePreference(message, 1L));
        assertEquals(BotState.SHOW_MAIN_MENU, map.get(1L).getBotState());
        assertEquals("Сударя", map.get(1L).getPreference());
    }
}
