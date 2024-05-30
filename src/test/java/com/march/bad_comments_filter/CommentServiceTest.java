package com.march.bad_comments_filter;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
import com.march.bad_comments_filter.dto.PredictionResponse;
import com.march.bad_comments_filter.repository.ReactiveCommentTagsRedisRepository;
import com.march.bad_comments_filter.service.CommentService;
import com.march.bad_comments_filter.service.MockCommentCategorizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    ReactiveCommentTagsRedisRepository commentRepository;
    CommentService commentService;

    @BeforeEach
    void setup() {
        commentService = new CommentService(commentRepository, new MockCommentCategorizer());
    }

    @Test
    @DisplayName("댓글에 대한 태그가 출력되는지 테스트")
    void getCommentTagsTest() {
        // given
        List<CommentRequest> commentRequests = List.of(
                new CommentRequest(1, "text1"),
                new CommentRequest(2, "text2"),
                new CommentRequest(3, "text3")
        );
        when(commentRepository.findByText(commentRequests.get(0)))
                .thenReturn(Mono.just(new CommentResponse(1, List.of(new PredictionResponse("test", 0.0)))));
        when(commentRepository.findByText(commentRequests.get(1)))
                .thenReturn(Mono.just(new CommentResponse(2, List.of(new PredictionResponse("test", 0.0)))));
        when(commentRepository.findByText(commentRequests.get(2)))
                .thenReturn(Mono.just(new CommentResponse(3, List.of(new PredictionResponse("test", 0.0)))));
        // expect
        commentService.getPredictionResults(commentRequests)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("캐시에 존재하지 않으면 categorizer 호출 후 캐시에 저장")
    void fallbackTest() {
        // given
        when(commentRepository.findByText(any())).thenReturn(Mono.empty());
        when(commentRepository.save(anyString(), anyList())).thenReturn(Mono.just(true));
        // when
        commentService.getPredictionResults(List.of(new CommentRequest(1, "cache miss")))
                .block();
        //then
        verify(commentRepository, times(1)).save(anyString(), anyList());
    }
}
