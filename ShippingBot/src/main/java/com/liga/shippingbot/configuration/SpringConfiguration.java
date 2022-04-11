package com.liga.shippingbot.configuration;

import com.liga.shippingbot.api.PersonRequest;
import com.liga.shippingbot.state.UserState;
import lombok.AllArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public Map<Long, PersonRequest> getMap() {
        return new HashMap<>();
    }


    @Bean
    public RestTemplate getRestTemplate() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        ClientHttpRequestFactory client = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(client);
    }

    @Bean
    public UserState userState() {
        return UserState.builder().build();
    }

}