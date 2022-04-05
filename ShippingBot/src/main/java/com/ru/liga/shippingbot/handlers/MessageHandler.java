package com.ru.liga.shippingbot.handlers;


import com.ru.liga.shippingbot.bot.BotState;
import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.entity.PersonModel;
import com.ru.liga.shippingbot.keyboard.ReplyKeyboardMaker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageHandler {

    final private ReplyKeyboardMaker replyKeyboardMaker;
    private String pathRequest = "ShippingBot/src/main/resources/img/background.jpg";
    @Getter
    Map<Long, Person> map = new HashMap<>();


    public BotApiMethod<?> startMessage(Message message) {
        Long longId = message.getFrom().getId();
        map.put(longId, new Person());
        map.get(longId).setId(longId);
        SendMessage sendMessage = new SendMessage(longId.toString(), "Вы сударь иль сударыня?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboardFirstStageForForm());
        log.info("log message: {}", "Пользователь запустил создание анкеты и создал объект person.");
        return sendMessage;
    }

    public BotApiMethod<?> getUserGender(Message message) {
        Long longId = message.getFrom().getId();
        if (message.getText().equals("Сударь") | message.getText().equals("Сударыня")) {
            map.get(longId).setGender(message.getText());
            SendMessage sendMessage = new SendMessage(longId.toString(), "Как вас величать?");
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
            log.info("log message: {}", "Пользователь продолжил создание анкеты и записал gender.");
            return sendMessage;
        }
        throw new RuntimeException("Ошибка: попытка внести в поле gender другой аргумент.");
    }

    public BotApiMethod<?> getUserName(Message message) {

        Long longId = message.getFrom().getId();
        map.get(longId).setName(message.getText());
        SendMessage sendMessage = new SendMessage(longId.toString(),
                "Опишите себя. (При этом первая строка считается " +
                        "Заголовком, все другие строки - Описанием," +
                        " если строка только одна, то Заголовком считается первое слово)\n?");
        log.info("log message: {}", "Пользователь продолжил создание анкеты и записал name.");
        return sendMessage;
    }

    public BotApiMethod<?> getUserDescription(Message message) {

        Long longId = message.getFrom().getId();
        map.get(longId).setDescription(message.getText());
        SendMessage sendMessage = new SendMessage(longId.toString(), "Кого вы ищите?");
        sendMessage.setReplyMarkup(replyKeyboardMaker.getKeyboardSecondStageForForm());
        log.info("log message: {}", "Пользователь продолжил создание анкеты и записал description.");
        return sendMessage;

    }

    public BotApiMethod<?> getUserPreference(Message message) {

        Long longId = message.getFrom().getId();
        if (message.getText().equals("Сударя") | message.getText().equals("Сударыню") |
                message.getText().equals("Всех")) {
            map.get(longId).setPreference(message.getText());

            RestTemplate restTemplate = createRestTemplate();
            Person person = map.get(longId);
            HttpEntity<Person> httpEntity = new HttpEntity<>(person);
            ResponseEntity<String> stringResponseEntity = restTemplate
                    .postForEntity("http://localhost:8484/server/person", httpEntity, String.class);

            String body = stringResponseEntity.getBody();            //необходимо получить картинку профиля НАДО ЛИ
            SendMessage sendMessage = new SendMessage(longId.toString(), "Поздравляем,вы заполнили анкету." + body);
            map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu
                    ("Анкета", "Поиск", "Любимцы"));
            log.info("log message: {}", "Пользователь завершил создание анкеты и записал preference и выполнил запрос" +
                    " http://localhost:8484/server/person");
            return sendMessage;
        }
        throw new RuntimeException("Ошибка: попытка внести в поле aim другой аргумент.");
    }

    public BotApiMethod<?> getSearch(Message message) {
        Long longId = message.getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        RestTemplate restTemplate = createRestTemplate();
        ResponseEntity<PersonModel> response = restTemplate
                .getForEntity("http://localhost:8484/server/person/{id}/show/lovers", PersonModel.class, longId);
        pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
        Person lover = response.getBody().getLover();
        if (!lover.getName().equals("")) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
        }
        map.get(longId).setBotState(BotState.SHOW_SEARCH);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Влево", "Меню", "Вправо"));
        log.info("log message: {}", "Пользователь нажал поиск и совершил запрос" +
                "http://localhost:8484/server/person/{id}/show/lovers");
        return sendMessage;
    }

    public BotApiMethod<?> getFavorite(Message message) {
        Long longId = message.getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        RestTemplate restTemplate = createRestTemplate();
        ResponseEntity<PersonModel> response = restTemplate
                .getForEntity("http://localhost:8484/server/{id}/preference/{action}", PersonModel.class, longId,
                        message.getText());
        pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();

        Person lover = response.getBody().getLover();
        if (!lover.getName().equals("")) {
            sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Влево", "Меню", "Вправо"));
        } else {
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        }
        map.get(longId).setBotState(BotState.SHOW_FAVORITE);

        log.info("log message: {}", "Пользователь нажал любимцы и совершил запрос" +
                "http://localhost:8484/server/{id}/preference/{action}");
        return sendMessage;
    }

    public BotApiMethod<?> getForm(Message message) {
        Long longId = message.getFrom().getId();
        try {
            RestTemplate restTemplate = createRestTemplate();
            ResponseEntity<PersonModel> response =
                    restTemplate.getForEntity("http://localhost:8484/server/person/{id}",
                            PersonModel.class, longId);

            map.put(longId, Objects.requireNonNull(response.getBody()).getLover());
            pathRequest = response.getBody().writeToPicture();

            String body = response.getBody().toString();
            SendMessage sendMessage = new SendMessage(longId.toString(), body);
            map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
            log.info("log message: {}", "Пользователь нажал анкета и совершил запрос" +
                    "http://localhost:8484/server/person/{id}");
            return sendMessage;
        } catch (HttpServerErrorException error) {
            throw new RuntimeException("Пользователь пытался получить анкету, не создав её.");
        }

    }

    public BotApiMethod<?> getLeft(Message message) {
        Long longId = message.getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        if (map.get(longId).getBotState().equals(BotState.SHOW_FAVORITE)) {
            RestTemplate restTemplate = createRestTemplate();
            ResponseEntity<PersonModel> response = restTemplate
                    .getForEntity("http://localhost:8484/server/{id}/preference/{action}", PersonModel.class, longId, message.getText());
            pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();

            Person lover = response.getBody().getLover();
            if (!lover.getName().equals("")) {
                sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Влево", "Меню", "Вправо"));
            } else {
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
            }
            log.info("log message: {}", "Пользователь нажал влево в режиме любимцев и совершил запрос" +
                    "http://localhost:8484/server/{id}/preference/{action}");
            return sendMessage;
        } else {
            RestTemplate restTemplate = createRestTemplate();
            Person p = new Person();
            p.setId(longId);
            HttpEntity<Person> httpEntity = new HttpEntity<>(p);
            ResponseEntity<PersonModel> response = restTemplate
                    .postForEntity("http://localhost:8484/server/person/dislike", httpEntity, PersonModel.class);
            pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
            Person lover = response.getBody().getLover();
            if (!lover.getName().equals("")) {
                sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Влево", "Меню", "Вправо"));
            } else {
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
            }
            log.info("log message: {}", "Пользователь нажал влево в режиме поиска и совершил запрос" +
                    "http://localhost:8484/server/person/dislike");
        }

        return sendMessage;
    }

    public BotApiMethod<?> getRight(Message message) {
        Long longId = message.getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        if (map.get(longId).getBotState().equals(BotState.SHOW_FAVORITE)) {
            RestTemplate restTemplate = createRestTemplate();
            ResponseEntity<PersonModel> response = restTemplate
                    .getForEntity("http://localhost:8484/server/{id}/preference/{action}", PersonModel.class, longId, message.getText());
            pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
            Person lover = response.getBody().getLover();
            if (!lover.getName().equals("")) {
                sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Влево", "Меню", "Вправо"));
            } else {
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
            }
            log.info("log message: {}", "Пользователь нажал вправо в режиме любимцев и совершил запрос" +
                    "http://localhost:8484/server/{id}/preference/{action}");
        } else {
            RestTemplate restTemplate = createRestTemplate();
            Person p = new Person();
            p.setId(longId);
            HttpEntity<Person> httpEntity = new HttpEntity<>(p);
            ResponseEntity<PersonModel> response = restTemplate
                    .postForEntity("http://localhost:8484/server/person/like", httpEntity, PersonModel.class);
            pathRequest = Objects.requireNonNull(response.getBody()).writeToPicture();
            Person lover = response.getBody().getLover();
            if (!lover.getName().equals("")) {
                sendMessage = new SendMessage(longId.toString(), lover.getGender() + " - " + lover.getName());
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Влево", "Меню", "Вправо"));
            }
            log.info("log message: {}", "Пользователь нажал вправо в режиме поиска и совершил запрос" +
                    "http://localhost:8484/server/person/like");
        }

        return sendMessage;
    }

    public BotApiMethod<?> getMenu(Message message) {
        Long longId = message.getFrom().getId();
        SendMessage sendMessage = new SendMessage(longId.toString(), "\n Используйте меню.");
        map.get(longId).setBotState(BotState.SHOW_MAIN_MENU);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMenu("Анкета", "Поиск", "Любимцы"));
        log.info("log message: {}", "Пользователь нажал кнопку меню");
        return sendMessage;
    }


    public BotApiMethod<?> getContinue(Message message) {
        Long longId = message.getFrom().getId();
        SendMessage sendMessage;
        try {
            RestTemplate restTemplate = createRestTemplate();
            ResponseEntity<PersonModel> response =
                    restTemplate.getForEntity("http://localhost:8484/server/person/{id}",
                            PersonModel.class, longId);
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
        log.info("log message: {}", "Пользователь ввёл /continue совершил запрос" +
                "http://localhost:8484/server/person/{id}");
        return sendMessage;
    }

    @SneakyThrows
    public SendPhoto sendPhoto(long chatId) {
        File image = ResourceUtils.getFile(pathRequest);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(image));
        sendPhoto.setChatId(String.valueOf(chatId));
        return sendPhoto;
    }

    private RestTemplate createRestTemplate() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        ClientHttpRequestFactory client = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(client);
    }
}