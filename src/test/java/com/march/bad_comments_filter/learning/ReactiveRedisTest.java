package com.march.bad_comments_filter.learning;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
class ReactiveRedisTest {
    @Autowired
    ReactiveStringRedisTemplate redisTemplate;

    ReactiveListOperations<String, String> opsForList;

    @BeforeEach
    void setup() {
        opsForList = redisTemplate.opsForList();
    }

    @AfterEach
    void teardown() {
        redisTemplate.scan(ScanOptions.scanOptions().match("comment:*").build())
                .subscribe(key -> redisTemplate.delete(key).subscribe());
    }

    @Test
    @DisplayName("키에 해당하는 리스트요소를 Flux로 가져오기")
    void opsForListTest() {
        // given
        String key = "comment:1";

        List<String> tags = List.of("this", "is", "bad", "comment");
        redisTemplate.opsForList().leftPushAll(key, tags).log().block();

        // expect
        StepVerifier.create(redisTemplate.opsForList().range(key, 0, -1).log())
                .expectNext("comment")
                .expectNext("bad")
                .expectNext("is")
                .expectNext("this")
                .verifyComplete();
    }
}
