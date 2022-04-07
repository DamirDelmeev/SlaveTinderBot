package com.ru.liga.shippingbot.handlers.template;

import com.ru.liga.shippingbot.entity.PersonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class RequestRunner {
    private final RestTemplate restTemplate;

    @Autowired
    public RequestRunner(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<PersonModel> runnerSearch(long longId) {
        log.info("log message: {}", "Пользователь нажал поиск и совершил запрос" +
                "http://localhost:7676/server/person/{id}/show/lovers");
        return restTemplate
                .getForEntity("http://localhost:7676/server/person/{id}/show/lovers", PersonModel.class, longId);
    }

    public ResponseEntity<PersonModel> runnerGetFavorite(Message message, Long longId) {
        log.info("log message: {}", "Пользователь нажал любимцы и совершил запрос" +
                "http://localhost:7676/server/{id}/preference/{action}");
        return restTemplate.getForEntity
                ("http://localhost:7676/server/{id}/preference/{action}", PersonModel.class, longId, message.getText());
    }

    public ResponseEntity<PersonModel> runnerGetForm(Long longId) {
        log.info("log message: {}", "Пользователь нажал анкета и совершил запрос" +
                "http://localhost:7676/server/person/{id}");
        return restTemplate.getForEntity("http://localhost:7676/server/person/{id}",
                PersonModel.class, longId);
    }

    public ResponseEntity<PersonModel> runnerGetLeftFavorite(Message message, Long longId) {
        log.info("log message: {}", "Пользователь нажал влево в режиме любимцев и совершил запрос" +
                "http://localhost:7676/server/{id}/preference/{action}");
        return restTemplate
                .getForEntity("http://localhost:7676/server/{id}/preference/{action}"
                        , PersonModel.class, longId, message.getText());
    }

    public ResponseEntity<PersonModel> runnerGetRightFavorite(Message message, Long longId) {
        log.info("log message: {}", "Пользователь нажал вправо в режиме любимцев и совершил запрос" +
                "http://localhost:7676/server/{id}/preference/{action}");
        return restTemplate
                .getForEntity("http://localhost:7676/server/{id}/preference/{action}", PersonModel.class, longId, message.getText());
    }

    public ResponseEntity<PersonModel> runnerGetDislike(HttpEntity httpEntity) {
        log.info("log message: {}", "Пользователь нажал влево в режиме поиска и совершил запрос" +
                "http://localhost:7676/server/person/dislike");
        return restTemplate
                .postForEntity("http://localhost:7676/server/person/dislike", httpEntity, PersonModel.class);
    }

    public ResponseEntity<PersonModel> runnerGetLike(HttpEntity httpEntity) {
        log.info("log message: {}", "Пользователь нажал вправо в режиме поиска и совершил запрос" +
                "http://localhost:7676/server/person/like");
        return restTemplate
                .postForEntity("http://localhost:7676/server/person/like", httpEntity, PersonModel.class);
    }

    public ResponseEntity<PersonModel> runnerGetContinue(Long longId) {
        log.info("log message: {}", "Пользователь ввёл /continue совершил запрос" +
                "http://localhost:7676/server/person/{id}");
        return restTemplate.getForEntity("http://localhost:7676/server/person/{id}",
                PersonModel.class, longId);
    }

    public void runnerGetUserPreference(HttpEntity httpEntity) {
        log.info("log message: {}", "Пользователь завершил создание анкеты и записал preference и выполнил запрос" +
                " http://localhost:7676/server/person");
        restTemplate.postForEntity("http://localhost:7676/server/person", httpEntity, String.class);
    }

    public void runnerAddChangeGender(HttpEntity httpEntity) {
        log.info("log message: {}", "Пользователь изменил gender и выполнил запрос " +
                "http://localhost:7676/server/update/person");
        restTemplate.put("http://localhost:7676/server/update/person", httpEntity);
    }

    public void runnerAddChangeName(HttpEntity httpEntity) {
        log.info("log message: {}", "Пользователь изменил name и выполнил запрос " +
                "http://localhost:7676/server/update/person");
        restTemplate.put("http://localhost:7676/server/update/person", httpEntity);
    }

    public void runnerAddChangeDescription(HttpEntity httpEntity) {
        log.info("log message: {}", "Пользователь изменил description и выполнил запрос " +
                "http://localhost:7676/server/update/person");
        restTemplate.put("http://localhost:7676/server/update/person", httpEntity);
    }

    public void runnerAddChangePreference(HttpEntity httpEntity) {
        log.info("log message: {}", "Пользователь изменил preference и выполнил запрос " +
                "http://localhost:7676/server/update/person");
        restTemplate.put("http://localhost:7676/server/update/person", httpEntity);
    }

}