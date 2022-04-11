package com.liga.shippingbot.services;


import com.liga.shippingbot.api.PersonRequest;
import com.liga.shippingbot.api.PersonResponse;
import com.liga.shippingbot.bot.BotState;
import com.liga.shippingbot.constants.MenuButton;
import com.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import com.liga.shippingbot.services.template.RequestRunner;
import com.liga.shippingbot.state.UserState;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
@Getter
@Data
public class MessageServiceImpl implements MessageService {

    private final ReplyKeyboardMaker replyKeyboardMaker;

    private byte[] formUser;

    private final RequestRunner requestRunner;

    private final FormServiceImpl formService;

    public BotApiMethod<?> getSearch(Long longId, UserState userState) {
        PersonResponse response = requestRunner.runnerGetSearch(longId);

        formUser = response.getBytesFromFile();
        PersonRequest lover = response.getUser();
        SendMessage sendMessage = new SendMessage();
        if (!lover.getName().isEmpty()) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
        }
        userState.setBotState(BotState.SHOW_SEARCH);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                (Arrays.asList(MenuButton.DISLIKE_BUTTON, MenuButton.MENU_BUTTON, MenuButton.LIKE_BUTTON)));
        return sendMessage;
    }


    public BotApiMethod<?> getFavorite(Message message, Long longId, UserState userState) {
        SendMessage sendMessage = new SendMessage();
        PersonResponse response = requestRunner.runnerGetFavorite(message, longId);
        formUser = response.getBytesFromFile();
        PersonRequest lover = response.getUser();
        if (!lover.getName().isEmpty()) {
            sendMessage = new SendMessage
                    (longId.toString(), lover.getGender() + " - " + lover.getName() + "\n" + response.getStatus());
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard(Arrays.asList(MenuButton.FAVORITE_LЕFT_BUTTON,
                    MenuButton.MENU_BUTTON, MenuButton.FAVORITE_RIGHT_BUTTON)));

        } else {
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
        }
        userState.setBotState(BotState.SHOW_FAVORITE);
        return sendMessage;
    }

    public BotApiMethod<?> getCommands(Long longId, UserState userState, Message message) {
        try {
            PersonResponse response = requestRunner.runnerGetForm(longId);
            PersonRequest user = response.getUser();
            if (user.getId() != null && user.getGender() != null && user.getName() != null
                    && user.getDescription() != null && user.getPreference() != null) {
                SendMessage sendMessage = new SendMessage(longId.toString(), response.toString());
                formUser = response.getBytesFromFile();
                userState.setBotState(BotState.SHOW_MAIN_MENU);
                sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                        (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
                return sendMessage;
            } else {
                return formService.startMessage(message, longId, userState);
            }
        } catch (HttpServerErrorException exception) {
            return new SendMessage(longId.toString(), "Чтобы получить анкету используйте " +
                    "команды:\n/start-создать анкету.\n/continue-у меня уже есть анкета.");
        }
    }

    public BotApiMethod<?> getForm(Long longId, UserState userState) {
        try {
            PersonResponse response = requestRunner.runnerGetForm(longId);
            userState.setId(longId);
            formUser = response.getBytesFromFile();
            SendMessage sendMessage = new SendMessage(longId.toString(), response.toString());
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.CHANGE_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
            return sendMessage;
        } catch (HttpServerErrorException error) {
            throw new RuntimeException("Пользователь пытался получить анкету, не создав её.");
        }
    }

    public BotApiMethod<?> getLeft(Message message, Long longId) {
        PersonResponse response = requestRunner.runnerGetLeftFavorite(message, longId);
        formUser = response.getBytesFromFile();
        PersonRequest lover = response.getUser();

        SendMessage sendMessage = new SendMessage();
        if (!lover.getName().isEmpty()) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName() + "\n" + response.getStatus());
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard(Arrays.asList(MenuButton.FAVORITE_LЕFT_BUTTON,
                    MenuButton.MENU_BUTTON, MenuButton.FAVORITE_RIGHT_BUTTON)));
        } else {
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
        }
        return sendMessage;
    }

    public BotApiMethod<?> getDislike(Long longId) {
        SendMessage sendMessage = new SendMessage();
        PersonRequest personRequest = new PersonRequest();
        personRequest.setId(longId);
        PersonResponse response = requestRunner.runnerGetDislike(personRequest);
        formUser = Objects.requireNonNull(response).getBytesFromFile();
        PersonRequest lover = response.getUser();
        if (!lover.getName().isEmpty()) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());

            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.DISLIKE_BUTTON, MenuButton.MENU_BUTTON, MenuButton.LIKE_BUTTON)));
        } else {
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
        }
        return sendMessage;
    }

    public BotApiMethod<?> getRight(Message message, Long longId) {
        PersonResponse response = requestRunner.runnerGetRightFavorite(message, longId);
        formUser = response.getBytesFromFile();
        PersonRequest lover = response.getUser();

        SendMessage sendMessage = new SendMessage();
        if (!lover.getName().isEmpty()) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName() + "\n" + response.getStatus());
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.FAVORITE_LЕFT_BUTTON, MenuButton.MENU_BUTTON, MenuButton.FAVORITE_RIGHT_BUTTON)));
        } else {
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
        }
        return sendMessage;
    }

    public BotApiMethod<?> getLike(Long longId, UserState userState) {
        PersonRequest personRequest = new PersonRequest();
        personRequest.setId(longId);

        PersonResponse response = requestRunner.runnerGetLike(personRequest);
        formUser = response.getBytesFromFile();
        PersonRequest lover = response.getUser();

        SendMessage sendMessage = new SendMessage();
        if (!response.getStatus().isEmpty()) {
            userState.setStatus(response.getStatus());
        }
        if (!lover.getName().isEmpty()) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.DISLIKE_BUTTON, MenuButton.MENU_BUTTON, MenuButton.LIKE_BUTTON)));

        }
        return sendMessage;
    }

    public BotApiMethod<?> getMenu(Long longId, UserState userState) {
        SendMessage sendMessage = new SendMessage(longId.toString(), "\n Используйте меню.");
        userState.setBotState(BotState.SHOW_MAIN_MENU);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
        log.info("log message: {}", "Пользователь нажал кнопку меню");
        return sendMessage;
    }

    public BotApiMethod<?> getContinue(Long longId, UserState userState) {
        try {
            PersonResponse response = requestRunner.runnerGetContinue(longId);
            PersonRequest lover = response.getUser();
            userState.setId(longId);
            formUser = response.getBytesFromFile();
            SendMessage sendMessage = new SendMessage(longId.toString(),
                    lover.getGender() + " - " + lover.getName() + "\nИспользуйте меню.");
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
            return sendMessage;
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Пользователь пытался получить анкету не создав её");
        }
    }

    @SneakyThrows
    public SendPhoto sendPhoto(Long longId) {
        InputStream inputStream = new ByteArrayInputStream(formUser);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(inputStream, "picture.jpg"));
        sendPhoto.setChatId(String.valueOf(longId));
        return sendPhoto;
    }

    public SendMessage sendStatus(Long longId, UserState userState) {
        SendMessage sendMessage = new SendMessage(longId.toString(), userState.getStatus());
        userState.setStatus("");
        return sendMessage;
    }
}