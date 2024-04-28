package com.march.bad_comments_filter.service;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Map;

@Component
public class MockCommentCategorizer implements CommentCategorizer {
    @Override
    public Mono<CommentResponse> categorize(CommentRequest commentRequest) {
        return Mono.just(new CommentResponse(commentRequest.id(), Map.of(
                        "tag1", 0.0,
                        "tag2", 0.0,
                        "tag3", 0.0,
                        "tag4", 0.0,
                        "tag5", 0.0,
                        "tag6", 0.0,
                        "tag7", 0.0,
                        "tag8", 0.0,
                        "tag9", 0.0,
                        "tag10", 0.0
                )))
                .publishOn(Schedulers.boundedElastic())
                .delayElement(Duration.ofMillis(200));
    }
}
