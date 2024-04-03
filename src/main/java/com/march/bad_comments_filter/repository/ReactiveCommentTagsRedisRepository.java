package com.march.bad_comments_filter.repository;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.exception.KeyGenerateException;
import com.march.bad_comments_filter.security.KeyGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Repository
@Profile("!perf-test")
public class ReactiveCommentTagsRedisRepository implements CommentRepository{
    private final ReactiveListOperations<String, String> opsForList;
    private final KeyGenerator keyGenerator;

    public ReactiveCommentTagsRedisRepository(ReactiveStringRedisTemplate redisTemplate, KeyGenerator keyGenerator) {
        this.opsForList = redisTemplate.opsForList();
        this.keyGenerator = keyGenerator;
    }

    @Override
    public Mono<CommentResponse> findByText(CommentRequest commentRequest) {
        String textHash = getKey(commentRequest.text());

        return getTags(textHash)
                .collectList()
                .flatMap(tags -> getResponseOrEmpty(commentRequest.id(), tags));
    }

    private Mono<CommentResponse> getResponseOrEmpty(String id, List<String> tags) {
        if (tags.isEmpty()) {
            return Mono.empty();
        }
        return Mono.just(new CommentResponse(id, tags));
    }

    private Flux<String> getTags(String key) {
        return opsForList.range(key, 0, -1);
    }

    @Override
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
