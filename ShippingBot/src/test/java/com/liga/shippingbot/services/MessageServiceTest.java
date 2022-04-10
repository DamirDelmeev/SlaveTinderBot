//package com.liga.shippingbot.services;
//
//import com.liga.shippingbot.bot.BotState;
//import com.liga.shippingbot.constants.MenuButton;
//import com.liga.shippingbot.entity.PersonRequest;
//import com.liga.shippingbot.keyboard.ReplyKeyboardMaker;
//import com.liga.shippingbot.services.template.RequestRunner;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class MessageServiceTest {
//    @Mock
//    RequestRunner requestRunner;
//    MessageServiceImpl messageService;
//    @Mock
//    ReplyKeyboardMaker replyKeyboardMaker;
//    @Mock
//    Map<Long, PersonRequest> userWithID;
//
//    public MessageServiceTest() {
//        MockitoAnnotations.openMocks(this);
//        this.messageService = new MessageServiceImpl(replyKeyboardMaker, userWithID, requestRunner);
//    }
//
//    @Test
//    void getMenu() {
//        Map<Long, PersonRequest> map = new HashMap<>();
//        messageService.getUserWithID().put(1L, new PersonRequest());
//        messageService.getMenu(1L);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("\n Используйте меню.");
//        sendMessage.setChatId("1");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard
//                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
//
//        assertEquals(sendMessage, messageService.getMenu(1L));
//        Assertions.assertEquals(BotState.SHOW_MAIN_MENU, map.get(1L).getBotState());
//        assertThrows(NullPointerException.class, () -> messageService.getMenu(2L));
//    }
//
//    @Test
//    void sendStatus() {
//        Map<Long, PersonRequest> map = new HashMap<>();
//        PersonRequest person = new PersonRequest();
//        person.setStatus("Статус");
//        messageService.getUserWithID().put(1L, person);
//        messageService.getMenu(1L);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Статус");
//        sendMessage.setChatId("1");
//
//
//        assertEquals(sendMessage, messageService.sendStatus(1L));
//        assertEquals("", map.get(1L).getStatus());
//        assertThrows(NullPointerException.class, () -> messageService.getMenu(2L));
//    }
//
//    @Test
//    void testGetMenu() {
//        Map<Long, PersonRequest> map = new HashMap<>();
//
//        messageService.getUserWithID().put(1L, new PersonRequest());
//
//        SendMessage sendMessage = new SendMessage("1", "\n Используйте меню.");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard
//                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
//
//        assertEquals(sendMessage, messageService.getMenu(1L));
//    }
//
//}
