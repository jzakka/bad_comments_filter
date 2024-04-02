package com.march.bad_comments_filter.service;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
public class MockCommentCategorizer implements CommentCategorizer{
    @Override
    public Mono<CommentResponse> categorize(CommentRequest commentRequest) {
        return Mono.just(new CommentResponse(commentRequest.id(), List.of("Mock", "Test")))
                .delayElement(Duration.ofMillis(100));
    }
}
