package com.march.bad_comments_filter.config;

import com.march.bad_comments_filter.security.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator("SHA-256");
    }
}
