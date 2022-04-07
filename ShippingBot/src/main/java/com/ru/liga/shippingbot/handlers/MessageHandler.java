package com.ru.liga.shippingbot.handlers;


import com.ru.liga.shippingbot.bot.BotState;
import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.entity.PersonModel;
import com.ru.liga.shippingbot.handlers.template.RequestRunner;
import com.ru.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.util.Map;
import java.util.Objects;

/**
 * Класс реализует основную часть обращений в базу данных.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class MessageHandler {
    /**
     * Объект класса меню с кнопками.
     */
    ReplyKeyboardMaker replyKeyboardMaker = new ReplyKeyboardMaker();
    /**
     * Поле: путь к картинке по запросу.
     */
    private String pathRequest = "src/main/resources/img/background.jpg";
    /**
     * Мапа с текущим пользователем.
     */
    Map<Long, Person> map;

    RequestRunner requestRunner;

    @Autowired
    public MessageHandler(RequestRunner requestRunner) {
        this.requestRunner = requestRunner;
    }

    @Autowired
    public void setMap(Map<Long, Person> map) {
        this.map = map;
    }

    /**
     * Метод реализует работу бота при нажатии кнопки Поиск.
     *
     * @param longId -идентификатор пользователя.
     */
    public BotApiMethod<?> getSearch(Long longId) {
        ResponseEntity<PersonModel> response = requestRunner.runnerSearch(longId);
        pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
        Person lover = response.getBody().getLover();

        SendMessage sendMessage = new SendMessage();
        if (!lover.getName().equals("")) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
        }
        map.get(longId).setBotState(BotState.SHOW_SEARCH);
        String like = EmojiParser.parseToUnicode(":hearts:");
        String disLike = EmojiParser.parseToUnicode(":heavy_multiplication_x:");

        sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu(disLike, "Меню", like));
        return sendMessage;
    }

    /**
     * Метод реализует работу бота при нажатии кнопки Любимцы.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */

    public BotApiMethod<?> getFavorite(Message message, Long longId) {
        SendMessage sendMessage = new SendMessage();
        ResponseEntity<PersonModel> response = requestRunner.runnerGetFavorite(message, longId);
        pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
        Person lover = response.getBody().getLover();
        if (!lover.getName().equals("")) {
            sendMessage = new SendMessage
                    (longId.toString(), lover.getGender() + " - " + lover.getName() + "\n" + response.getBody().getStatus());
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Влево", "Меню", "Вправо"));
        } else {
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        }
        map.get(longId).setBotState(BotState.SHOW_FAVORITE);
        return sendMessage;
    }

    /**
     * Метод реализует работу бота при нажатии кнопки Анкета.
     *
     * @param longId -идентификатор пользователя.
     */
    public BotApiMethod<?> getForm(Long longId) {
        try {
            ResponseEntity<PersonModel> response = requestRunner.runnerGetForm(longId);
            map.put(longId, Objects.requireNonNull(response.getBody()).getLover());
            pathRequest = response.getBody().writeToPicture();

            String body = response.getBody().toString();
            SendMessage sendMessage = new SendMessage(longId.toString(), body);
            map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu
                    ("Изменить анкету", "Поиск", "Любимцы"));
            return sendMessage;
        } catch (HttpServerErrorException error) {
            throw new RuntimeException("Пользователь пытался получить анкету, не создав её.");
        }
    }

    /**
     * Метод реализует работу бота при нажатии кнопки Влево.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> getLeft(Message message, Long longId) {
        ResponseEntity<PersonModel> response = requestRunner.runnerGetLeftFavorite(message, longId);
        pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
        Person lover = response.getBody().getLover();

        SendMessage sendMessage = new SendMessage();
        if (!lover.getName().equals("")) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName() + "\n" + response.getBody().getStatus());
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Влево", "Меню", "Вправо"));
        } else {
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        }
        return sendMessage;

    }

    /**
     * Метод реализует работу бота при нажатии кнопки dislike.
     *
     * @param longId -идентификатор пользователя, сообщение от пользователя.
     */

    public BotApiMethod<?> getDislike(Long longId) {
        SendMessage sendMessage = new SendMessage();
        Person p = new Person();
        p.setId(longId);
        HttpEntity<Person> httpEntity = new HttpEntity<>(p);
        ResponseEntity<PersonModel> response = requestRunner.runnerGetDislike(httpEntity);
        pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
        Person lover = response.getBody().getLover();
        if (!lover.getName().equals("")) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
            String like = EmojiParser.parseToUnicode(":hearts:");
            String disLike = EmojiParser.parseToUnicode(":heavy_multiplication_x:");
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu(disLike, "Меню", like));
        } else {
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        }
        return sendMessage;
    }

    /**
     * Метод реализует работу бота при нажатии кнопки Вправо.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> getRight(Message message, Long longId) {
        ResponseEntity<PersonModel> response = requestRunner.runnerGetRightFavorite(message, longId);
        pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
        Person lover = response.getBody().getLover();

        SendMessage sendMessage = new SendMessage();
        if (!lover.getName().equals("")) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName() + "\n" + response.getBody().getStatus());
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Влево", "Меню", "Вправо"));
        } else {
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        }
        return sendMessage;
    }

    /**
     * Метод реализует работу бота при нажатии кнопки like.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    public BotApiMethod<?> getLike(Long longId) {
        Person person = new Person();
        person.setId(longId);
        HttpEntity<Person> httpEntity = new HttpEntity<>(person);
        ResponseEntity<PersonModel> response = requestRunner.runnerGetLike(httpEntity);
        pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
        Person lover = response.getBody().getLover();

        SendMessage sendMessage = new SendMessage();
        if (!response.getBody().getStatus().isEmpty()) {
            map.get(longId).setStatus(response.getBody().getStatus());
        }
        if (!lover.getName().equals("")) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
            String like = EmojiParser.parseToUnicode(":hearts:");
            String disLike = EmojiParser.parseToUnicode(":heavy_multiplication_x:");

            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu(disLike, "Меню", like));
        }
        return sendMessage;
    }

    /**
     * Метод реализует работу бота при нажатии кнопки Меню.
     *
     * @param longId -идентификатор пользователя.
     */
    public BotApiMethod<?> getMenu(Long longId) {
        SendMessage sendMessage = new SendMessage(longId.toString(), "\n Используйте меню.");
        map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        log.info("log message: {}", "Пользователь нажал кнопку меню");
        return sendMessage;
    }

    /**
     * Метод реализует работу бота при вводе команды "/continue".
     *
     * @param longId -идентификатор пользователя.
     */

    public BotApiMethod<?> getContinue(Long longId) {
        SendMessage sendMessage;
        try {
            ResponseEntity<PersonModel> response = requestRunner.runnerGetContinue(longId);
            Person lover = Objects.requireNonNull(response.getBody()).getLover();
            map.put(longId, lover);
            pathRequest = response.getBody().writeToPicture();
            sendMessage = new SendMessage(longId.toString(),
                    lover.getGender() + " - " + lover.getName() + "\nИспользуйте меню.");
            map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Пользователь пытался получить анкету не создав её");
        }
        return sendMessage;
    }

    /**
     * Метод реализует отправление картинки по запросу.
     *
     * @param longId -идентификатор пользователя.
     */
    @SneakyThrows
    public SendPhoto sendPhoto(Long longId) {
        File image = ResourceUtils.getFile(pathRequest);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(image));
        sendPhoto.setChatId(String.valueOf(longId));
        return sendPhoto;
    }

    /**
     * Метод реализует получение статуса пользователя по запросу.
     *
     * @param longId -идентификатор пользователя.
     */
    public SendMessage sendStatus(Long longId) {
        SendMessage sendMessage = new SendMessage(longId.toString(), map.get(longId).getStatus());
        map.get(longId).setStatus("");
        return sendMessage;
    }
}