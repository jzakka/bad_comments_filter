package com.march.bad_comments_filter.repository;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import reactor.core.publisher.Mono;

import java.util.Map;


public interface CommentRepository {
    Mono<CommentResponse> findByText(CommentRequest commentRequest);

    Mono<Boolean> save(String text, Map<String, Double> labelPrediction);
}
