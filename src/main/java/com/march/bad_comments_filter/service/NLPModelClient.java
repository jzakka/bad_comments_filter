package com.march.bad_comments_filter.service;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.dto.PredictionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NLPModelClient implements CommentCategorizer {
    private final WebClient webClient;
    @Override
    public Mono<CommentResponse> categorize(CommentRequest commentRequest) {
        return webClient.post()
                .uri("/api/classify")
                .bodyValue(commentRequest.text())
                .exchangeToMono(res -> {
                    if (res.statusCode().equals(HttpStatus.OK)) {
                        return res
                                .bodyToMono(new ParameterizedTypeReference<List<PredictionResponse>>() {})
                                .map(labelPrediction -> new CommentResponse(commentRequest.id(), labelPrediction));
                    }
                    return res.createError();
                });
    }
}
