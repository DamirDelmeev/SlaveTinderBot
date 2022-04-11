package com.liga.shippingbot.services.template;

import com.liga.shippingbot.api.PersonRequest;
import com.liga.shippingbot.api.PersonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestRunner {
    private final RestTemplate restTemplate;

    @Value("${telegram.host-url}")
    private String url;

    public PersonResponse runnerGetSearch(long longId) {
        log.info("log message: {}", "Пользователь нажал поиск");
        return restTemplate
                .getForObject(url + "{id}/search", PersonResponse.class, longId);
    }

    public PersonResponse runnerGetFavorite(Message message, Long longId) {
        log.info("log message: {}", "Пользователь нажал любимцы");
        return restTemplate.getForObject(url + "{id}/favorites/{action}", PersonResponse.class, longId, message.getText());
    }

    public PersonResponse runnerGetForm(Long longId) {
        log.info("log message: {}", "Пользователь нажал анкета");
        return restTemplate.getForObject(url + "{id}",
                PersonResponse.class, longId);
    }

    public PersonResponse runnerGetLeftFavorite(Message message, Long longId) {
        log.info("log message: {}", "Пользователь нажал влево в режиме любимцев");
        return restTemplate
                .getForObject(url + "{id}/favorites/{action}"
                        , PersonResponse.class, longId, message.getText());
    }

    public PersonResponse runnerGetRightFavorite(Message message, Long longId) {
        log.info("log message: {}", "Пользователь нажал вправо в режиме любимцев");
        return restTemplate
                .getForObject(url + "{id}/favorites/{action}", PersonResponse.class, longId, message.getText());
    }

    public PersonResponse runnerGetDislike(PersonRequest personRequest) {
        log.info("log message: {}", "Пользователь нажал влево в режиме поиска");
        return restTemplate
                .postForObject(url + "attitude/{action}", personRequest, PersonResponse.class, "dislike");
    }

    public PersonResponse runnerGetLike(PersonRequest personRequest) {

        log.info("log message: {}", "Пользователь нажал вправо в режиме поиска");
        return restTemplate
                .postForObject(url + "attitude/{action}", personRequest, PersonResponse.class, "like");
    }

    public PersonResponse runnerGetContinue(Long longId) {
        try {
            log.info("log message: {}", "Пользователь ввёл /continue");
            return restTemplate.getForObject(url + "{id}",
                    PersonResponse.class, longId);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public void runnerPostUser(PersonRequest personRequest) {
        log.info("log message: {}", "Пользователь завершил создание анкеты и записал preference ");
        restTemplate.postForEntity(url + "", personRequest, String.class);
    }

    public void runnerPutUser(PersonRequest personRequest) {
        log.info("log message: {}", "Пользователь изменил поля");
        restTemplate.put(url + "", personRequest);
    }

    public void runnerDelete(Long longId) {
        log.info("log message: {}", "Пользователь создаёт новую анкету");
        restTemplate.delete(url + "{id}", longId);
    }
}