//package com.liga.shippingbot.services;
//
//import com.liga.shippingbot.bot.BotState;
//import com.liga.shippingbot.constants.Gender;
//import com.liga.shippingbot.constants.MenuButton;
//import com.liga.shippingbot.constants.Preferences;
//import com.liga.shippingbot.entity.PersonRequest;
//import com.liga.shippingbot.keyboard.ReplyKeyboardMaker;
//import com.liga.shippingbot.services.template.RequestRunner;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
//
//import java.util.Arrays;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class ChangeServiceTest {
//    @Mock
//    RequestRunner requestRunner;
//    @Mock
//    ReplyKeyboardMaker replyKeyboardMaker;
//
//    ChangeServiceImpl changeService;
//    @Mock
//    Map<Long, PersonRequest> userWithID;
//
//    public ChangeServiceTest() {
//        MockitoAnnotations.openMocks(this);
//        this.changeService = new ChangeServiceImpl(replyKeyboardMaker, userWithID, requestRunner);
//    }
//
//    @Test
//    void getChangeFormFirstStageTestBotStateAndReturn() {
//
//        userWithID.put(1L, new PersonRequest());
//
//        changeService.getChangeFormFirstStage(1L);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Укажите поле, которое хотите изменить.");
//        sendMessage.setChatId("1");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard(Arrays.asList(MenuButton.CHANGE_GENDER_BUTTON,
//                MenuButton.CHANGE_NAME_BUTTON, MenuButton.CHANGE_DESCRIPTION_BUTTON,
//                MenuButton.CHANGE_PREFERENCE_BUTTON, MenuButton.MENU_BUTTON)));
//
//        assertEquals(sendMessage, changeService.getChangeFormFirstStage(1L));
//        Assertions.assertEquals(BotState.SHOW_CHANGES, userWithID.get(1L).getBotState());
//        assertThrows(NullPointerException.class, () -> changeService.getChangeFormFirstStage(2L));
//    }
//
//    @Test
//    void getChangeGenderSecondStageTestBotStateAndReturn() {
//
//        userWithID.put(1L, new PersonRequest());
//
//
//        changeService.getChangeGenderSecondStage(1L);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Вы сударь иль сударыня?");
//        sendMessage.setChatId("1");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard(Arrays.asList(Gender.MALE, Gender.FEMALE)));
//        assertEquals(sendMessage, changeService.getChangeGenderSecondStage(1L));
//        Assertions.assertEquals(BotState.CHANGE_GENDER, userWithID.get(1L).getBotState());
//        assertThrows(NullPointerException.class, () -> changeService.getChangeGenderSecondStage(2L));
//    }
//
//    @Test
//    void getChangeNameSecondStageTestBotStateAndReturn() {
//
//        userWithID.put(1L, new PersonRequest());
//
//        changeService.getChangeNameSecondStage(1L);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Как вас зовут?");
//        sendMessage.setChatId("1");
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
//
//        assertEquals(sendMessage, changeService.getChangeNameSecondStage(1L));
//        Assertions.assertEquals(BotState.CHANGE_NAME, userWithID.get(1L).getBotState());
//        assertThrows(NullPointerException.class, () -> changeService.getChangeNameSecondStage(2L));
//    }
//
//    @Test
//    void getChangeDescriptionSecondStageTestBotStateAndReturn() {
//
//        userWithID.put(1L, new PersonRequest());
//
//        changeService.getChangeDescriptionSecondStage(1L);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Опишите себя.");
//        sendMessage.setChatId("1");
//        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
//
//        assertEquals(sendMessage, changeService.getChangeDescriptionSecondStage(1L));
//        Assertions.assertEquals(BotState.CHANGE_DESCRIPTION, userWithID.get(1L).getBotState());
//        assertThrows(NullPointerException.class, () -> changeService.getChangeDescriptionSecondStage(2L));
//    }
//
//    @Test
//    void getChangePreferenceSecondStage() {
//
//        userWithID.put(1L, new PersonRequest());
//        ChangeServiceImpl changeService = new ChangeServiceImpl(replyKeyboardMaker, userWithID, requestRunner);
//
//        changeService.getChangePreferenceSecondStage(1L);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Кого вы ищите?");
//        sendMessage.setChatId("1");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard
//                (Arrays.asList(Preferences.ALL_AIM, Preferences.FEMALE_AIM, Preferences.MALE_AIM)));
//
//        assertEquals(sendMessage, changeService.getChangePreferenceSecondStage(1L));
//        Assertions.assertEquals(BotState.CHANGE_PREFERENCE, userWithID.get(1L).getBotState());
//        assertThrows(NullPointerException.class, () -> changeService.getChangePreferenceSecondStage(2L));
//    }
//
//    @Test
//    void addChangeGenderTest() {
//        Message message = new Message();
//        message.setText("ля ля");
//
//        userWithID.put(1L, new PersonRequest());
//
//
//        assertThrows(RuntimeException.class, () -> changeService.addChangeGender(message, 1L));
//
//        message.setText("Сударь");
//        SendMessage sendMessage = new SendMessage("1", "Вы успешно внесли изменение.");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard
//                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
//
//        assertEquals(sendMessage, changeService.addChangeGender(message, 1L));
//        Assertions.assertEquals(BotState.SHOW_MAIN_MENU, userWithID.get(1L).getBotState());
//        assertEquals("Сударь", userWithID.get(1L).getGender());
//    }
//
//    @Test
//    void addChangeNameTest() {
//        Message message = new Message();
//        message.setText("ля ля");
//
//        userWithID.put(1L, new PersonRequest());
//
//
//        SendMessage sendMessage = new SendMessage("1", "Вы успешно внесли изменение.");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard
//                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
//
//        assertEquals(sendMessage, changeService.addChangeName(message, 1L));
//        Assertions.assertEquals(BotState.SHOW_MAIN_MENU, userWithID.get(1L).getBotState());
//        assertEquals("ля ля", userWithID.get(1L).getName());
//    }
//
//    @Test
//    void addChangeDescription() {
//        Message message = new Message();
//        message.setText("ля ля");
//
//        userWithID.put(1L, new PersonRequest());
//
//
//        SendMessage sendMessage = new SendMessage("1", "Вы успешно внесли изменение.");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard
//                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
//
//        assertEquals(sendMessage, changeService.addChangeDescription(message, 1L));
//        Assertions.assertEquals(BotState.SHOW_MAIN_MENU, userWithID.get(1L).getBotState());
//        assertEquals("ля ля", userWithID.get(1L).getDescription());
//    }
//
//    @Test
//    void addChangePreference() {
//        Message message = new Message();
//        message.setText("ля ля");
//
//        userWithID.put(1L, new PersonRequest());
//
//
//        assertThrows(RuntimeException.class, () -> changeService.addChangePreference(message, 1L));
//
//        message.setText("Сударя");
//        SendMessage sendMessage = new SendMessage("1", "Вы успешно внесли изменение.");
//        sendMessage.setReplyMarkup(new ReplyKeyboardMaker().getKeyboard
//                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
//
//
//        assertEquals(sendMessage, changeService.addChangePreference(message, 1L));
//        Assertions.assertEquals(BotState.SHOW_MAIN_MENU, userWithID.get(1L).getBotState());
//        assertEquals("Сударя", userWithID.get(1L).getPreference());
//    }
//}
