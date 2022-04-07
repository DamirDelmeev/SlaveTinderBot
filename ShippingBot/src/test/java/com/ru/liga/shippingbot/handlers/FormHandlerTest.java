package com.ru.liga.shippingbot.handlers;

import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.handlers.template.RequestRunner;
import com.ru.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormHandlerTest {
    @Mock
    RequestRunner requestRunner;

    FormHandler formHandler;

    public FormHandlerTest() {
        MockitoAnnotations.openMocks(this);
        this.formHandler = new FormHandler(requestRunner);
    }

    @Test
    void getStart() {
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());

        formHandler.setMap(map);
        formHandler.startMessage(1L);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Вы сударь иль сударыня?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboardFirstStageForForm());

        assertEquals(sendMessage, formHandler.startMessage(1L));
        Person actual = map.get(1L);
        assertEquals(1L, actual.getId());
    }


    @Test
    void getUserGender() {
        Message message = new Message();
        message.setText("ля-ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());

        formHandler.setMap(map);

        assertThrows(RuntimeException.class, () -> formHandler.getUserGender(message, 1L));


        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Как вас величать?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        message.setText("Сударь");
        assertEquals(sendMessage, formHandler.getUserGender(message, 1L));
        assertEquals("Сударь", map.get(1L).getGender());
        assertThrows(NullPointerException.class, () -> formHandler.getUserGender(message, 2L));
    }

    @Test
    void getUserName() {
        Message message = new Message();
        message.setText("ля-ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        formHandler.setMap(map);
        map.get(1L).setGender("Сударь");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Опишите себя.");
        sendMessage.setChatId("1");

        assertEquals(sendMessage, formHandler.getUserName(message, 1L));
        assertEquals("ля-ля", map.get(1L).getName());
        assertThrows(NullPointerException.class, () -> formHandler.getUserDescription(message, 2L));
    }

    @Test
    void getUserDescription() {
        Message message = new Message();
        message.setText("ля-ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        formHandler.setMap(map);
        map.get(1L).setGender("Сударь");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Кого вы ищите?");
        sendMessage.setChatId("1");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboardSecondStageForForm());

        assertEquals(sendMessage, formHandler.getUserDescription(message, 1L));
        assertEquals("ля-ля", map.get(1L).getDescription());
        assertThrows(NullPointerException.class, () -> formHandler.getUserDescription(message, 2L));
    }

    @Test
    void getUserPreferenceRandomMessageTest() {
        Message message = new Message();
        message.setText("ля-ля");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        formHandler.setMap(map);
        assertThrows(RuntimeException.class, () -> formHandler.getUserPreference(message, 1L));
    }

    @Test
    void getUserPreferenceTest() {
        Message message = new Message();
        message.setText("Сударя");
        Map<Long, Person> map = new HashMap<>();
        map.put(1L, new Person());
        formHandler.setMap(map);

        SendMessage sendMessage = new SendMessage("1", "Поздравляем,вы заполнили анкету.");
        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getMenu
                ("Анкета", "Поиск", "Любимцы"));

        assertEquals(sendMessage, formHandler.getUserPreference(message, 1L));
    }
}


