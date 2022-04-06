package com.ru.liga.shippingbot.handlers.template;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class Rest {
    public RestTemplate createRestTemplate() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        ClientHttpRequestFactory client = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(client);
    }
}
