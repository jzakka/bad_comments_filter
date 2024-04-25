package com.march.bad_comments_filter.config;

import com.march.bad_comments_filter.security.KeyGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator("SHA-256");
    }

    @Bean
    public WebClient webClient(@Value("${model.url}") String baseUrl) {
        return WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .build();
    }
}
