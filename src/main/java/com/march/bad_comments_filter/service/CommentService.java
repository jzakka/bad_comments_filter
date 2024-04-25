package com.march.bad_comments_filter.service;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentCategorizer categorizer;

    public ParallelFlux<CommentResponse> getCommentTags(List<CommentRequest> commentRequests) {
        return Flux.fromIterable(commentRequests)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(this::getSingleResult);
    }

    private Mono<CommentResponse> getSingleResult(CommentRequest commentRequest) {
        return commentRepository.findByText(commentRequest)
                .switchIfEmpty(Mono.defer(() -> fallback(commentRequest)));
    }

    private Mono<CommentResponse> fallback(CommentRequest commentRequest) {
        return categorizer.categorize(commentRequest)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(res -> commentRepository.save(commentRequest.text(), res.labelPrediction()).subscribe());
    }
}
