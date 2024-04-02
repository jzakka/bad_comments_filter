package com.march.bad_comments_filter.controller;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.ParallelFlux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value = "/api/blind", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    ParallelFlux<CommentResponse> blindBadComments(@RequestBody List<CommentRequest> commentRequests) {
        return commentService.getCommentTags(commentRequests);
    }
}
