package com.march.bad_comments_filter.repository;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.exception.KeyGenerateException;
import com.march.bad_comments_filter.security.KeyGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Profile("!perf-test")
public class ReactiveCommentTagsRedisRepository implements CommentRepository{
    private final ReactiveHashOperations<String, String, String> opsForHash;
    private final KeyGenerator keyGenerator;

    public ReactiveCommentTagsRedisRepository(ReactiveStringRedisTemplate redisTemplate, KeyGenerator keyGenerator) {
        this.opsForHash = redisTemplate.opsForHash();
        this.keyGenerator = keyGenerator;
    }

    @Override
    public Mono<CommentResponse> findByText(CommentRequest commentRequest) {
        String textHash = getKey(commentRequest.text());

        return getPredictedResult(textHash)
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .flatMap(predicted -> getResponseOrEmpty(commentRequest.id(), predicted));
    }

    private Mono<CommentResponse> getResponseOrEmpty(String id, Map<String, String> predictedResult) {
        if (predictedResult.isEmpty()) {
            return Mono.empty();
        }
        return Mono.just(new CommentResponse(id, convertDoubleValue(predictedResult)));
    }

    private Flux<Map.Entry<String, String>> getPredictedResult(String key) {
        return opsForHash.entries(key);
    }

    @Override
    public Mono<Boolean> save(String text, Map<String, Double> labelPrediction) {
        String textNotNull = Objects.requireNonNull(text, "Text cannot be null");

        String textHash = getKey(textNotNull);

        return opsForHash.putAll(textHash, convertStringValue(labelPrediction));
    }

    private Map<String, String> convertStringValue(Map<String, Double> labelPrediction) {
        return labelPrediction.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, Double> convertDoubleValue(Map<String, String> labelPrediction) {
        return labelPrediction.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), Double.valueOf(e.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String getKey(String text) {
        try {
            return keyGenerator.generate(text);
        } catch (NoSuchAlgorithmException e) {
            throw new KeyGenerateException("Cannot generate a Key");
        }
    }
}
