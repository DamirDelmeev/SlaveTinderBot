package com.ru.liga.shippingbot.configuration;

import com.ru.liga.shippingbot.entity.Person;
import lombok.AllArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class SpringConfiguration {
    private final TelegramConfig telegramConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookPath()).build();
    }

    @Bean
    @Scope("singleton")
    public Map<Long, Person> setMap() {
        return new HashMap<>();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        ClientHttpRequestFactory client = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(client);
    }
}