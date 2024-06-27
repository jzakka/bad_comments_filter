package com.march.bad_comments_filter.cache;

import com.march.bad_comments_filter.dto.PredictionResponse;
import com.march.bad_comments_filter.repository.ReactiveCommentTagsRedisRepository;
import com.march.bad_comments_filter.security.KeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import java.util.List;

// 캐싱된 시나리오에서 부하테스트를 위해 미리 댓글들을 캐시해놓습니다.
// 부하테스트에서 댓글 텍스트는 "캐싱된 댓글.${N}" 이어야 합니다. N은 0이상 1만 미만입니다.
public class CacheCommentsForPerfTest {
    LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory("localhost", 6379);

    ReactiveStringRedisTemplate redisTemplate = new ReactiveStringRedisTemplate(connectionFactory);

    ReactiveListOperations<String, String> opsForList;

    ReactiveCommentTagsRedisRepository repository = new ReactiveCommentTagsRedisRepository(redisTemplate, new KeyGenerator("SHA-256"));

    @BeforeEach
    void setup() {
        connectionFactory.start();
        opsForList = redisTemplate.opsForList();
    }

    @Test
    @DisplayName("미리 1만개의 데이터들을 캐시")
    void setCachedComments () {
        String comment = "캐싱된 댓글.";
        for (int i = 0; i < 10_000; i++) {
            repository.save(comment + i, List.of(
                    new PredictionResponse("라벨1", 0.0),
                    new PredictionResponse("라벨2", 0.0),
                    new PredictionResponse("라벨3", 0.0),
                    new PredictionResponse("라벨4", 0.0),
                    new PredictionResponse("라벨5", 0.0),
                    new PredictionResponse("라벨6", 0.0)
            )).block();
        }
    }
}
