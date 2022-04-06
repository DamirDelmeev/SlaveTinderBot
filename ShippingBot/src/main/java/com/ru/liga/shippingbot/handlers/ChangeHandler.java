package com.ru.liga.shippingbot.handlers;

import com.ru.liga.shippingbot.bot.BotState;
import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.handlers.template.Rest;
import com.ru.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.Map;

/**
 * Класс реализует обработку изменений в анкету.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChangeHandler {
    /**
     * Объект класса меню с кнопками.
     */
    ReplyKeyboardMaker replyKeyboardMaker = new ReplyKeyboardMaker();
    /**
     * Мапа с текущим пользователем.
     */
    @Getter
    Map<Long, Person> map;

    @Autowired
    public void setMap(Map<Long, Person> map) {
        this.map = map;
    }

    /**
     * Метод предоставляет пользователю кнопки на выбор для изменений.
     *
     * @param longId -идентификатор пользователя.
     */
    public BotApiMethod<?> getChangeFormFirstStage(Long longId) {
        map.get(longId).setBotState(BotState.SHOW_CHANGES);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Укажите поле, которое хотите изменить.");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu
                ("Пол", "Имя", "Описание", "Приоритет поиска"));
        log.info("log message: {}", "Пользователь нажал кнопку изменить анкету");
        return sendMessage;
    }

    /**
     * Метод предоставляет пользователю кнопки на выбор для изменений поля:gender.
     *
     * @param longId -идентификатор пользователя.
     */
    public BotApiMethod<?> getChangeGenderSecondStage(Long longId) {
        map.get(longId).setBotState(BotState.CHANGE_GENDER);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Вы сударь иль сударыня?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboardFirstStageForForm());
        log.info("log message: {}", "Пользователь выбрал поле gender для изменений.");
        return sendMessage;
    }

    /**
     * Метод предоставляет пользователю возможность внести изменения в поле:name.
     *
     * @param longId -идентификатор пользователя.
     */
    public BotApiMethod<?> getChangeNameSecondStage(Long longId) {
        map.get(longId).setBotState(BotState.CHANGE_NAME);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Как вас зовут?");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        log.info("log message: {}", "Пользователь выбрал поле name для изменений.");
        return sendMessage;
    }

    /**
     * Метод предоставляет пользователю возможность внести изменения в поле:description.
     *
     * @param longId -идентификатор пользователя.
     */
    public BotApiMethod<?> getChangeDescriptionSecondStage(Long longId) {
        map.get(longId).setBotState(BotState.CHANGE_DESCRIPTION);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Опишите себя.");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        log.info("log message: {}", "Пользователь выбрал поле description для изменений.");
        return sendMessage;
    }

    /**
     * Метод предоставляет пользователю кнопки на выбор для изменений поля:preference.
     *
     * @param longId -идентификатор пользователя.
     */
    public BotApiMethod<?> getChangePreferenceSecondStage(Long longId) {
        map.get(longId).setBotState(BotState.CHANGE_PREFERENCE);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Кого вы ищите?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboardSecondStageForForm());
        log.info("log message: {}", "Пользователь выбрал поле preference для изменений.");
        return sendMessage;
    }

    /**
     * Метод вносит изменения в поле:gender.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> addChangeGender(Message message, Long longId) {
        if (message.getText().equals("Сударь") || message.getText().equals("Сударыня")) {
            map.get(longId).setGender(message.getText());
            RestTemplate restTemplate = new Rest().createRestTemplate();
            HttpEntity<Person> person = new HttpEntity<>(map.get(longId));
            restTemplate.put("http://localhost:7676/server/update/person", person);
            map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
            SendMessage sendMessage = new SendMessage(longId.toString(), "Вы успешно внесли изменение.");
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
            log.info("log message: {}", "Пользователь изменил gender и выполнил запрос " +
                    "http://localhost:7676/server/update/person");
            return sendMessage;
        } else {
            throw new RuntimeException("Пользователь не внёс изменение.");
        }
    }

    /**
     * Метод вносит изменения в поле:name.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> addChangeName(Message message, Long longId) {
        map.get(longId).setName(message.getText());
        RestTemplate restTemplate = new Rest().createRestTemplate();
        HttpEntity<Person> person = new HttpEntity<>(map.get(longId));
        restTemplate.put("http://localhost:7676/server/update/person", person);
        map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Вы успешно внесли изменение.");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        log.info("log message: {}", "Пользователь изменил name и выполнил запрос " +
                "http://localhost:7676/server/update/person");
        return sendMessage;
    }

    /**
     * Метод вносит изменения в поле:description.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> addChangeDescription(Message message, Long longId) {
        map.get(longId).setDescription(message.getText());
        RestTemplate restTemplate = new Rest().createRestTemplate();
        HttpEntity<Person> person = new HttpEntity<>(map.get(longId));
        restTemplate.put("http://localhost:7676/server/update/person", person);
        map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Вы успешно внесли изменение.");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        log.info("log message: {}", "Пользователь изменил description и выполнил запрос " +
                "http://localhost:7676/server/update/person");
        return sendMessage;
    }

    /**
     * Метод вносит изменения в поле:preference.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> addChangePreference(Message message, Long longId) {
        if (message.getText().equals("Сударя") || message.getText().equals("Сударыню") || message.getText().equals("Всех")) {
            map.get(longId).setPreference(message.getText());
            RestTemplate restTemplate = new Rest().createRestTemplate();
            HttpEntity<Person> person = new HttpEntity<>(map.get(longId));
            restTemplate.put("http://localhost:7676/server/update/person", person);
            map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
            SendMessage sendMessage = new SendMessage(longId.toString(), "Вы успешно внесли изменение.");
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
            log.info("log message: {}", "Пользователь изменил preference и выполнил запрос " +
                    "http://localhost:7676/server/update/person");
            return sendMessage;
        } else {
            throw new RuntimeException("Пользователь не внёс изменение.");
        }
    }
}
