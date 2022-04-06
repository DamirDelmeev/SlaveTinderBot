package com.ru.liga.shippingbot.handlers;

import com.ru.liga.shippingbot.bot.BotState;
import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.handlers.template.Rest;
import com.ru.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import lombok.Getter;
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
 * Класс реализует инициализацию анкеты.
 */
@Component
@Slf4j
public class FormHandler {
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
     * Метод предоставляет пользователю кнопки на выбор для выбора поля gender.
     *
     * @param longId -идентификатор пользователя.
     */
    public BotApiMethod<?> startMessage(Long longId) {
        map.put(longId, new Person());
        map.get(longId).setId(longId);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Вы сударь иль сударыня?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboardFirstStageForForm());
        log.info("log message: {}", "Пользователь запустил создание анкеты и создал объект person.");
        return sendMessage;
    }

    /**
     * Метод предоставляет пользователю возможность внести изменение поля name.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> getUserGender(Message message, Long longId) {
        if (message.getText().equals("Сударь") | message.getText().equals("Сударыня")) {
            map.get(longId).setGender(message.getText());
            SendMessage sendMessage = new SendMessage(longId.toString(), "Как вас величать?");
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
            log.info("log message: {}", "Пользователь продолжил создание анкеты и записал gender.");
            return sendMessage;
        }
        throw new RuntimeException("Ошибка: попытка внести в поле gender другой аргумент.");
    }

    /**
     * Метод предоставляет пользователю возможность внести изменение поля description.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> getUserName(Message message, Long longId) {
        map.get(longId).setName(message.getText());
        SendMessage sendMessage = new SendMessage(longId.toString(),
                "Опишите себя. (При этом первая строка считается " +
                        "Заголовком, все другие строки - Описанием," +
                        " если строка только одна, то Заголовком считается первое слово)\n?");
        log.info("log message: {}", "Пользователь продолжил создание анкеты и записал name.");
        return sendMessage;
    }

    /**
     * Метод предоставляет пользователю кнопки на выбор для выбора поля preference.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> getUserDescription(Message message, Long longId) {
        map.get(longId).setDescription(message.getText());
        SendMessage sendMessage = new SendMessage(longId.toString(), "Кого вы ищите?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboardSecondStageForForm());
        log.info("log message: {}", "Пользователь продолжил создание анкеты и записал description.");
        return sendMessage;
    }

    /**
     * Метод производит инициализацию пользователя и вносит его в базу данных.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> getUserPreference(Message message, Long longId) {
        if (message.getText().equals("Сударя") | message.getText().equals("Сударыню") |
                message.getText().equals("Всех")) {
            map.get(longId).setPreference(message.getText());

            RestTemplate restTemplate = new Rest().createRestTemplate();
            Person person = map.get(longId);
            HttpEntity<Person> httpEntity = new HttpEntity<>(person);
            restTemplate.postForEntity("http://localhost:7676/server/person", httpEntity, String.class);

            SendMessage sendMessage = new SendMessage(longId.toString(), "Поздравляем,вы заполнили анкету.");
            map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu
                    ("Анкета", "Поиск", "Любимцы"));
            log.info("log message: {}", "Пользователь завершил создание анкеты и записал preference и выполнил запрос" +
                    " http://localhost:7676/server/person");
            return sendMessage;
        }
        throw new RuntimeException("Ошибка: попытка внести в поле aim другой аргумент.");
    }
}
