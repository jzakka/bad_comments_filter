package com.march.bad_comments_filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BadCommentsFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BadCommentsFilterApplication.class, args);
    }

}
