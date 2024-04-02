package com.march.bad_comments_filter.repository;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.exception.KeyGenerateException;
import com.march.bad_comments_filter.security.KeyGenerator;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Repository
public class ReactiveCommentTagsRedisRepository {
    private final ReactiveListOperations<String, String> opsForList;
    private final KeyGenerator keyGenerator;

    public ReactiveCommentTagsRedisRepository(ReactiveStringRedisTemplate redisTemplate, KeyGenerator keyGenerator) {
        this.opsForList = redisTemplate.opsForList();
        this.keyGenerator = keyGenerator;
    }

    public Mono<CommentResponse> findByText(CommentRequest commentRequest) {
        String textHash = getKey(commentRequest.text());

        return Mono.just(commentRequest)
                .flatMap(rq -> opsForList.range(textHash, 0, -1).collectList())
                .map(tags -> new CommentResponse(commentRequest.id(), tags));
    }

    public Mono<Long> save(String text, List<String> tags) {
        String textNotNull = Objects.requireNonNull(text, "Text cannot be null");

        String textHash = getKey(textNotNull);

        return opsForList.leftPushAll(textHash, tags);
    }

    private String getKey(String text) {
        try {
            return keyGenerator.generate(text);
        } catch (NoSuchAlgorithmException e) {
            throw new KeyGenerateException("Cannot generate a Key");
        }
    }
}
