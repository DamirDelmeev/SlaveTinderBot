package com.liga.shippingbot.services;

import com.liga.shippingbot.api.PersonRequest;
import com.liga.shippingbot.api.PersonResponse;
import com.liga.shippingbot.bot.BotState;
import com.liga.shippingbot.constants.Gender;
import com.liga.shippingbot.constants.MenuButton;
import com.liga.shippingbot.constants.Preferences;
import com.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import com.liga.shippingbot.services.template.RequestRunner;
import com.liga.shippingbot.state.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.Arrays;


@Component
@RequiredArgsConstructor
@Slf4j
public class ChangeServiceImpl implements ChangeService {

    private final ReplyKeyboardMaker replyKeyboardMaker;

    private final RequestRunner requestRunner;


    public BotApiMethod<?> getChangeFormFirstStage(Long longId, UserState userState) {
        userState.setBotState(BotState.CHANGES);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Укажите поле, которое хотите изменить.");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard(Arrays.asList(MenuButton.CHANGE_GENDER_BUTTON,
                MenuButton.CHANGE_NAME_BUTTON, MenuButton.CHANGE_DESCRIPTION_BUTTON,
                MenuButton.CHANGE_PREFERENCE_BUTTON, MenuButton.MENU_BUTTON)));
        log.info("log message: {}", "Пользователь нажал кнопку изменить анкету");
        return sendMessage;
    }

    public BotApiMethod<?> getChangeGenderSecondStage(Long longId, UserState userState) {
        userState.setBotState(BotState.CHANGE_GENDER);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Вы сударь иль сударыня?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard(Arrays.asList(Gender.MALE, Gender.FEMALE)));
        log.info("log message: {}", "Пользователь выбрал поле gender для изменений.");
        return sendMessage;
    }


    public BotApiMethod<?> getChangeNameSecondStage(Long longId, UserState userState) {
        userState.setBotState(BotState.CHANGE_NAME);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Как вас зовут?");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        log.info("log message: {}", "Пользователь выбрал поле name для изменений.");
        return sendMessage;
    }


    public BotApiMethod<?> getChangeDescriptionSecondStage(Long longId, UserState userState) {
        userState.setBotState(BotState.CHANGE_DESCRIPTION);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Опишите себя.");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        log.info("log message: {}", "Пользователь выбрал поле description для изменений.");
        return sendMessage;
    }


    public BotApiMethod<?> getChangePreferenceSecondStage(Long longId, UserState userState) {
        userState.setBotState(BotState.CHANGE_PREFERENCE);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Кого вы ищите?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                (Arrays.asList(Preferences.ALL_AIM, Preferences.FEMALE_AIM, Preferences.MALE_AIM)));
        log.info("log message: {}", "Пользователь выбрал поле preference для изменений.");
        return sendMessage;
    }


    public BotApiMethod<?> addChangeGender(Message message, Long longId, UserState userState) {
        if (Gender.matches(message.getText())) {
            PersonResponse personResponse = requestRunner.runnerGetContinue(longId);
            if (personResponse == null) {
                throw new RuntimeException("Пользователь не найден.");
            }
            PersonRequest person = personResponse.getLover().toBuilder().gender(message.getText()).build();
            requestRunner.runnerPutUser(person);
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            SendMessage sendMessage = new SendMessage(longId.toString(), "Вы успешно внесли изменение.");
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
            return sendMessage;
        } else {
            throw new RuntimeException("Пользователь не внёс изменение.");
        }
    }


    public BotApiMethod<?> addChangeName(Message message, Long longId, UserState userState) {
        PersonResponse personResponse = requestRunner.runnerGetContinue(longId);
        if (personResponse == null) {
            throw new RuntimeException("Пользователь не найден.");
        }
        PersonRequest person = personResponse.getLover().toBuilder().name(message.getText()).build();
        requestRunner.runnerPutUser(person);
        userState.setBotState(BotState.SHOW_MAIN_MENU);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Вы успешно внесли изменение.");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
        return sendMessage;
    }


    public BotApiMethod<?> addChangeDescription(Message message, Long longId, UserState userState) {
        PersonResponse personResponse = requestRunner.runnerGetContinue(longId);
        if (personResponse == null) {
            throw new RuntimeException("Пользователь не найден.");
        }
        PersonRequest person = personResponse.getLover().toBuilder().description(message.getText()).build();
        requestRunner.runnerPutUser(person);
        userState.setBotState(BotState.SHOW_MAIN_MENU);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Вы успешно внесли изменение.");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
        return sendMessage;
    }


    public BotApiMethod<?> addChangePreference(Message message, Long longId, UserState userState) {
        if (Preferences.matches(message.getText())) {
            PersonResponse personResponse = requestRunner.runnerGetContinue(longId);
            if (personResponse == null) {
                throw new RuntimeException("Пользователь не найден.");
            }
            PersonRequest person = personResponse.getLover().toBuilder().preference(message.getText()).build();
            requestRunner.runnerPutUser(person);
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            SendMessage sendMessage = new SendMessage(longId.toString(), "Вы успешно внесли изменение.");
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
            return sendMessage;
        } else {
            throw new RuntimeException("Пользователь не внёс изменение.");
        }
    }
}
