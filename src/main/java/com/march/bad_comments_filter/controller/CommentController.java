package com.march.bad_comments_filter.controller;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://gall.dcinside.com",  "https://n.news.naver.com"}, originPatterns = {"https://*.youtube.com"})
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value = "/api/blind-old")
    public Mono<List<CommentResponse>> blindBadComments(@RequestBody List<CommentRequest> commentRequests) {
        return commentService.getPredictionResults(commentRequests);
    }

    @PostMapping(value = "/api/blind")
    public Mono<CommentResponse> blindBadComment(@RequestBody CommentRequest commentRequest) {
        return commentService.getSingleResult(commentRequest);
    }
}
