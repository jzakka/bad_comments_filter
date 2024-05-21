package com.march.bad_comments_filter.repository;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.dto.PredictionResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Profile("test")
@Repository
public class MockCommentRepository implements CommentRepository{
    @Override
    public Mono<CommentResponse> findByText(CommentRequest commentRequest) {
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> save(String text, List<PredictionResponse> labelPrediction) {
        return Mono.just(true);
    }
}
