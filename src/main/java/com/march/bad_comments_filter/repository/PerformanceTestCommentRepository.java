package com.march.bad_comments_filter.repository;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@Profile("perf-test")
public class PerformanceTestCommentRepository implements CommentRepository{
    @Override
    public Mono<CommentResponse> findByText(CommentRequest commentRequest) {
        return Mono.empty();
    }

    @Override
    public Mono<Long> save(String text, List<String> tags) {
        return Mono.just(1L);
    }
}
