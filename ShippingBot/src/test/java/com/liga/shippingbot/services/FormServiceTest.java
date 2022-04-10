//package com.liga.shippingbot.services;
//
//import com.liga.shippingbot.constants.Gender;
//import com.liga.shippingbot.constants.MenuButton;
//import com.liga.shippingbot.constants.Preferences;
//import com.liga.shippingbot.entity.PersonRequest;
//import com.liga.shippingbot.keyboard.ReplyKeyboardMaker;
//import com.liga.shippingbot.services.template.RequestRunner;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
//
//import javax.ws.rs.client.AsyncInvoker;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class FormServiceTest {
//    @Mock
//    RequestRunner requestRunner;
//    @Mock
//    ReplyKeyboardMaker replyKeyboardMaker;
//
//    FormServiceImpl formService;
//    @Mock
//    Map<Long, PersonRequest> userWithID;
//
//    public FormServiceTest() {
//        MockitoAnnotations.openMocks(this);
//        this.formService = new FormServiceImpl(replyKeyboardMaker, userWithID, requestRunner);
//    }
//
//    @Test
//    void getStart() {
//
//
//
//        userWithID.put(1L, new PersonRequest());
//        formService.startMessage(1L);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Вы сударь иль сударыня?");
//        sendMessage.setChatId("1");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard(Arrays.asList(Gender.MALE, Gender.FEMALE)));
//
//        assertEquals(sendMessage, formService.startMessage(1L));
//        PersonRequest actual = userWithID.get(1L);
//        assertEquals(1L, actual.getId());
//    }
//
//
//    @Test
//    void getUserGender() {
//        Message message = new Message();
//        message.setText("ля-ля");
//
//        userWithID.put(1L, new PersonRequest());
//
//
//        assertThrows(RuntimeException.class, () -> formService.getUserGender(message, 1L));
//
//
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Как вас величать?");
//        sendMessage.setChatId("1");
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
//        message.setText("Сударь");
//        assertEquals(sendMessage, formService.getUserGender(message, 1L));
//        assertEquals("Сударь", userWithID.get(1L).getGender());
//        assertThrows(NullPointerException.class, () -> formService.getUserGender(message, 2L));
//    }
//
//    @Test
//    void getUserName() {
//        Message message = new Message();
//        message.setText("ля-ля");
//
//        userWithID.put(1L, new PersonRequest());
//        userWithID.get(1L).setGender("Сударь");
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Опишите себя.");
//        sendMessage.setChatId("1");
//
//        assertEquals(sendMessage, formService.getUserName(message, 1L));
//        assertEquals("ля-ля", userWithID.get(1L).getName());
//        assertThrows(NullPointerException.class, () -> formService.getUserDescription(message, 2L));
//    }
//
//    @Test
//    void getUserDescription() {
//        Message message = new Message();
//        message.setText("ля-ля");
//        userWithID.put(1L, new PersonRequest());
//
//        userWithID.get(1L).setGender("Сударь");
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Кого вы ищите?");
//        sendMessage.setChatId("1");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard
//                (Arrays.asList(Preferences.ALL_AIM, Preferences.FEMALE_AIM, Preferences.MALE_AIM)));
//
//        assertEquals(sendMessage, formService.getUserDescription(message, 1L));
//        assertEquals("ля-ля", userWithID.get(1L).getDescription());
//        assertThrows(NullPointerException.class, () -> formService.getUserDescription(message, 2L));
//    }
//
//    @Test
//    void getUserPreferenceRandomMessageTest() {
//        Message message = new Message();
//        message.setText("ля-ля");
//        userWithID.put(1L, new PersonRequest());
//
//        assertThrows(RuntimeException.class, () -> formService.getUserPreference(message, 1L));
//    }
//
//    @Test
//    void getUserPreferenceTest() {
//        Message message = new Message();
//        message.setText("Сударя");
//
//        userWithID.put(1L, new PersonRequest());
//
//
//        SendMessage sendMessage = new SendMessage("1", "Поздравляем,вы заполнили анкету.");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard
//                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
//
//        assertEquals(sendMessage, formService.getUserPreference(message, 1L));
//    }
//}
//
//
