package com.march.bad_comments_filter;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.repository.ReactiveCommentTagsRedisRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IntegrationTest {
    @Autowired
    ReactiveStringRedisTemplate redisTemplate;
    ReactiveListOperations<String, String> opsForList;
    @Autowired
    ReactiveCommentTagsRedisRepository repository;

    @BeforeEach
    void setup() {
        clear();
        opsForList = redisTemplate.opsForList();
    }

    @AfterEach
    void teardown() {
        clear();
    }

    void clear() {
        redisTemplate.scan(ScanOptions.scanOptions().match("*").build())
                .flatMap(key -> redisTemplate.delete(key))
                .collectList() // 모든 delete 연산을 리스트로 모음
                .block(); // 모든 삭제 연산이 완료될 때까지 기다림
    }

    @Test
    @DisplayName("레디스에 데이터 삽입")
    void insertTest() {
        // given
        String text = "This is comment.";
        List<String> tags = List.of("tag1", "tag2", "tag3");

        // when
        repository.save(text, tags)

        // then
                .as(StepVerifier::create)
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    @DisplayName("레디스에서 데이터 조회")
    void getTest() {
        // given
        String text = "This is comment.";
        List<String> tags = List.of("tag1", "tag2", "tag3");
        repository.save(text, tags).block();

        // expect
        repository.findByText(new CommentRequest("comment1", text))
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertThat(response.id()).isEqualTo("comment1");
                    assertThat(response.tags()).containsExactlyInAnyOrder("tag1", "tag2", "tag3");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("존재하지 않는 키로 조회시 Mono empty")
    void getByEmptyTest() {
        // given
        String text = "EMPTY";

        // expect
        repository.findByText(new CommentRequest("comment-emtpy", text))
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
