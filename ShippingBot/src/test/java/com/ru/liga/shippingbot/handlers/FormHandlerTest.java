package com.ru.liga.shippingbot.handlers;

import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormHandlerTest {

    @Test
    void getStart() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        FormHandler formHandler = new FormHandler();
        formHandler.setMap(map);
        formHandler.startMessage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Вы сударь иль сударыня?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboardFirstStageForForm());
        BotApiMethod<?> botApiMethod = sendMessage;

        assertEquals(botApiMethod, formHandler.startMessage(1L));
        Person actual = map.get(1L);
        assertEquals(1L, actual.getId());
    }


    @Test
    void getUserGender() {
        Message message = new Message();
        message.setText("ля-ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        FormHandler formHandler = new FormHandler();
        formHandler.setMap(map);

        assertThrows(RuntimeException.class, () -> formHandler.getUserGender(message, 1L));


        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Как вас величать?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        BotApiMethod<?> botApiMethod = sendMessage;
        message.setText("Сударь");
        assertEquals(botApiMethod, formHandler.getUserGender(message, 1L));
        assertEquals("Сударь", map.get(1L).getGender());
        assertThrows(NullPointerException.class, () -> formHandler.getUserGender(message, 2L));
    }

    @Test
    void getUserName() {
        Message message = new Message();
        message.setText("ля-ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        FormHandler formHandler = new FormHandler();
        formHandler.setMap(map);
        map.get(1L).setGender("Сударь");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Опишите себя.");
        sendMessage.setChatId("1");
        BotApiMethod<?> botApiMethod = sendMessage;

        assertEquals(botApiMethod, formHandler.getUserName(message, 1L));
        assertEquals("ля-ля", map.get(1L).getName());
        assertThrows(NullPointerException.class, () -> formHandler.getUserDescription(message, 2L));
    }

    @Test
    void getUserDescription() {
        Message message = new Message();
        message.setText("ля-ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        FormHandler formHandler = new FormHandler();
        formHandler.setMap(map);
        map.get(1L).setGender("Сударь");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Кого вы ищите?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboardSecondStageForForm());
        BotApiMethod<?> botApiMethod = sendMessage;

        assertEquals(botApiMethod, formHandler.getUserDescription(message, 1L));
        assertEquals("ля-ля", map.get(1L).getDescription());
        assertThrows(NullPointerException.class, () -> formHandler.getUserDescription(message, 2L));
    }
}

