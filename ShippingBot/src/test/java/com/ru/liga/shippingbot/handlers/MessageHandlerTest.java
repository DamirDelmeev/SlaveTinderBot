package com.ru.liga.shippingbot.handlers;

import com.ru.liga.shippingbot.bot.BotState;
import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.handlers.template.RequestRunner;
import com.ru.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageHandlerTest {
    @Mock
    RequestRunner requestRunner;
    MessageHandler messageHandler;

    public MessageHandlerTest() {
        MockitoAnnotations.openMocks(this);
        this.messageHandler = new MessageHandler(requestRunner);

    }

    @Test
    void getMenu() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        messageHandler.setMap(map);
        messageHandler.getMenu(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("\n Используйте меню.");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker()
                .getMenu("Анкета", "Поиск", "Любимцы"));

        assertEquals(sendMessage, messageHandler.getMenu(1L));
        assertEquals(BotState.SHOW_MAIN_MENU, map.get(1L).getBotState());
        assertThrows(NullPointerException.class, () -> messageHandler.getMenu(2L));
    }

    @Test
    void sendStatus() {
        Map<Long, Person> map = new HashMap<>();
        Person person = new Person();
        person.setStatus("Статус");
        map.put(1L, person);
        messageHandler.setMap(map);
        messageHandler.getMenu(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Статус");
        sendMessage.setChatId("1");


        assertEquals(sendMessage, messageHandler.sendStatus(1L));
        assertEquals("", map.get(1L).getStatus());
        assertThrows(NullPointerException.class, () -> messageHandler.getMenu(2L));
    }

    @Test
    void testGetMenu() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        messageHandler.setMap(map);

        SendMessage sendMessage = new SendMessage("1", "\n Используйте меню.");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getMenu("Анкета", "Поиск", "Любимцы"));

        assertEquals(sendMessage, messageHandler.getMenu(1L));
    }

}
