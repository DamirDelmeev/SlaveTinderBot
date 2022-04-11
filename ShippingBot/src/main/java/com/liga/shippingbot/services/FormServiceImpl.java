package com.liga.shippingbot.services;

import com.liga.shippingbot.api.PersonRequest;
import com.liga.shippingbot.api.PersonResponse;
import com.liga.shippingbot.bot.BotState;
import com.liga.shippingbot.constants.Commands;
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
@Slf4j
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final ReplyKeyboardMaker replyKeyboardMaker;

    private final RequestRunner requestRunner;

    public BotApiMethod<?> startMessage(Message message, Long longId, UserState userState) {
        PersonResponse personResponse = requestRunner.runnerGetContinue(longId);
        userState.setBotState(BotState.CREATING_STATE);
        PersonRequest user = personResponse.getUser();
        if (user.getId() != null && user.getGender() != null && user.getName() != null
                && user.getDescription() != null && user.getPreference() != null
                && message.getText().equals(Commands.START.getName())) {
            requestRunner.runnerDelete(longId);
            personResponse = null;
        }
        if (personResponse == null) {
            return createNewPerson(longId);
        } else {
            if (personResponse.getUser().getGender() == null || personResponse.getUser().getGender().isEmpty()) {
                return getUserGender(message, personResponse.getUser());
            } else if (personResponse.getUser().getName() == null || personResponse.getUser().getName().isEmpty()) {
                return getUserName(message, personResponse.getUser());
            } else if (personResponse.getUser().getDescription() == null || personResponse.getUser().getDescription().isEmpty()) {
                return getUserDescription(message, personResponse.getUser());
            } else if (personResponse.getUser().getPreference() == null || personResponse.getUser().getPreference().isEmpty()) {
                return getUserPreference(message, personResponse.getUser(), userState);
            }
        }
        return null;
    }

    private SendMessage createNewPerson(Long longId) {
        PersonRequest personRequest = PersonRequest.builder().id(longId).build();
        requestRunner.runnerPostUser(personRequest);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Вы сударь иль сударыня?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard(Arrays.asList(Gender.MALE, Gender.FEMALE)));
        log.info("log message: {}", "Пользователь запустил создание анкеты и создал объект person.");
        return sendMessage;
    }

    private BotApiMethod<?> getUserGender(Message message, PersonRequest personRequest) {
        if (message.getText().toLowerCase().equals(Gender.MALE.getName().toLowerCase())
                | message.getText().toLowerCase().equals(Gender.FEMALE.getName().toLowerCase())) {
            requestRunner.runnerPutUser(personRequest.toBuilder().gender(message.getText()).build());
            SendMessage sendMessage = new SendMessage(personRequest.getId().toString(), "Как вас величать?");
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
            log.info("log message: {}", "Пользователь продолжил создание анкеты и записал gender.");
            return sendMessage;
        }
        return new SendMessage(message.getFrom().getId().toString(),
                "Доступно:\n" + Gender.FEMALE.getName() + "\n" + Gender.MALE.getName());
    }

    private BotApiMethod<?> getUserName(Message message, PersonRequest personRequest) {
        requestRunner.runnerPutUser(personRequest.toBuilder().name(message.getText()).build());
        SendMessage sendMessage = new SendMessage(personRequest.getId().toString(),
                "Опишите себя.");
        log.info("log message: {}", "Пользователь продолжил создание анкеты и записал name.");
        return sendMessage;
    }

    private BotApiMethod<?> getUserDescription(Message message, PersonRequest personRequest) {
        requestRunner.runnerPutUser(personRequest.toBuilder().description(message.getText()).build());
        SendMessage sendMessage = new SendMessage(personRequest.getId().toString(), "Кого вы ищите?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                (Arrays.asList(Preferences.ALL_AIM, Preferences.FEMALE_AIM, Preferences.MALE_AIM)));
        log.info("log message: {}", "Пользователь продолжил создание анкеты и записал description.");
        return sendMessage;
    }

    private BotApiMethod<?> getUserPreference(Message message, PersonRequest personRequest, UserState userState) {
        if (Preferences.matches(message.getText())) {
            requestRunner.runnerPutUser(personRequest.toBuilder().preference(message.getText()).build());

            SendMessage sendMessage = new SendMessage(personRequest.getId().toString(), "Поздравляем,вы заполнили анкету.");
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboard
                    (Arrays.asList(MenuButton.FORM_BUTTON, MenuButton.SEARCH_BUTTON, MenuButton.FAVORITE_BUTTON)));
            return sendMessage;
        }
        return new SendMessage(message.getFrom().getId().toString(),
                "Доступно:\n" + Preferences.ALL_AIM.getName() + "\n"
                        + Preferences.FEMALE_AIM.getName() + "\n" + Preferences.MALE_AIM.getName());
    }
}
