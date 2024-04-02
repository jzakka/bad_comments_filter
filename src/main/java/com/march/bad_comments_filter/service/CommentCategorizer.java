package com.march.bad_comments_filter.service;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import reactor.core.publisher.Mono;

public interface CommentCategorizer {
    Mono<CommentResponse> categorize(CommentRequest commentRequest);
}
