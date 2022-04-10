package com.liga.shippingbot.services.template;

import com.liga.shippingbot.api.PersonRequest;
import com.liga.shippingbot.api.PersonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestRunner {//исправить
    private final RestTemplate restTemplate;

    public PersonResponse runnerSearch(long longId) {
        log.info("log message: {}", "Пользователь нажал поиск и совершил запрос");
        ResponseEntity<PersonResponse> response = restTemplate
                .getForEntity("http://localhost:7979/server/person/{id}/show/lovers", PersonResponse.class, longId);
        if (!response.getStatusCode().toString().equals("200 OK")) {
            throw new RuntimeException("Ошибка запроса");
        }
        return response.getBody();

    }

    public PersonResponse runnerGetFavorite(Message message, Long longId) {
        log.info("log message: {}", "Пользователь нажал любимцы и совершил запрос");
        ResponseEntity<PersonResponse> response = restTemplate.getForEntity
                ("http://localhost:7979/server/{id}/preference/{action}", PersonResponse.class, longId, message.getText());
        if (!response.getStatusCode().toString().equals("200 OK")) {
            throw new RuntimeException("Ошибка запроса");
        }
        return response.getBody();
    }

    public PersonResponse runnerGetForm(Long longId) {
        log.info("log message: {}", "Пользователь нажал анкета и совершил запрос");
        ResponseEntity<PersonResponse> response = restTemplate.getForEntity("http://localhost:7979/server/person/{id}",
                PersonResponse.class, longId);
        if (!response.getStatusCode().toString().equals("200 OK")) {
            throw new RuntimeException("Ошибка запроса");
        }
        return response.getBody();
    }

    public PersonResponse runnerGetLeftFavorite(Message message, Long longId) {
        log.info("log message: {}", "Пользователь нажал влево в режиме любимцев и совершил запрос");
        ResponseEntity<PersonResponse> response = restTemplate
                .getForEntity("http://localhost:7979/server/{id}/preference/{action}"
                        , PersonResponse.class, longId, message.getText());
        if (!response.getStatusCode().toString().equals("200 OK")) {
            throw new RuntimeException("Ошибка запроса");
        }
        return response.getBody();
    }

    public PersonResponse runnerGetRightFavorite(Message message, Long longId) {
        log.info("log message: {}", "Пользователь нажал вправо в режиме любимцев и совершил запрос");
        ResponseEntity<PersonResponse> response = restTemplate
                .getForEntity("http://localhost:7979/server/{id}/preference/{action}", PersonResponse.class, longId, message.getText());
        if (!response.getStatusCode().toString().equals("200 OK")) {
            throw new RuntimeException("Ошибка запроса");
        }
        return response.getBody();
    }

    public PersonResponse runnerGetDislike(PersonRequest personRequest) {
        log.info("log message: {}", "Пользователь нажал влево в режиме поиска и совершил запрос");
        ResponseEntity<PersonResponse> response = restTemplate
                .postForEntity("http://localhost:7979/server/person/dislike", personRequest, PersonResponse.class);
        if (!response.getStatusCode().toString().equals("200 OK")) {
            throw new RuntimeException("Ошибка запроса");
        }
        return response.getBody();
    }

    public PersonResponse runnerGetLike(PersonRequest personRequest) {

        log.info("log message: {}", "Пользователь нажал вправо в режиме поиска и совершил запрос");
        ResponseEntity<PersonResponse> response = restTemplate
                .postForEntity("http://localhost:7979/server/person/like", personRequest, PersonResponse.class);
        if (!response.getStatusCode().toString().equals("200 OK")) {
            throw new RuntimeException("Ошибка запроса");
        }
        return response.getBody();
    }

    public PersonResponse runnerGetContinue(Long longId) {
        try {


            log.info("log message: {}", "Пользователь ввёл /continue совершил запрос");
            ResponseEntity<PersonResponse> response = restTemplate.getForEntity("http://localhost:7979/server/person/{id}",
                    PersonResponse.class, longId);
            if (!response.getStatusCode().toString().equals("200 OK")) {
                throw new RuntimeException("Ошибка запроса");
            }

            return response.getBody();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }


    public void runnerPostUser(PersonRequest personRequest) {
        log.info("log message: {}", "Пользователь завершил создание анкеты и записал preference и выполнил запрос");
        restTemplate.postForEntity("http://localhost:7979/server/person", personRequest, String.class);
    }


    public void runnerPutUser(PersonRequest personRequest) {
        log.info("log message: {}", "Пользователь изменил поля");
        restTemplate.put("http://localhost:7979/server/update/person", personRequest);
    }
//    public ResponseEntity<PersonResponse> runnerDelete(Long longId) {
//        log.info("log message: {}", "Пользователь нажал анкета и совершил запрос");
//        return restTemplate.delete("http://localhost:7979/server/person/{id}",longId);
//    }


}