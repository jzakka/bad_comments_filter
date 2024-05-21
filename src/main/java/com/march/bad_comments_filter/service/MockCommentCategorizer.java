package com.march.bad_comments_filter.service;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.dto.PredictionResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Profile("test")
@Component
public class MockCommentCategorizer implements CommentCategorizer {
    @Override
    public Mono<CommentResponse> categorize(CommentRequest commentRequest) {
        return Mono.just(new CommentResponse(commentRequest.id(), List.of(
                        new PredictionResponse("tag1", 0.0),
                        new PredictionResponse("tag2", 0.0),
                        new PredictionResponse("tag3", 0.0),
                        new PredictionResponse("tag4", 0.0),
                        new PredictionResponse("tag5", 0.0),
                        new PredictionResponse("tag6", 0.0),
                        new PredictionResponse("tag7", 0.0),
                        new PredictionResponse("tag8", 0.0),
                        new PredictionResponse("tag9", 0.0),
                        new PredictionResponse("tag10", 0.0)
                )))
                .publishOn(Schedulers.boundedElastic())
                .delayElement(Duration.ofMillis(200));
    }
}
