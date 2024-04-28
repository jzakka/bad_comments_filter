package com.march.bad_comments_filter;

import com.march.bad_comments_filter.dto.CommentRequest;
import com.march.bad_comments_filter.dto.CommentResponse;
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
import java.util.Map;

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
                new CommentRequest("test-id1", "text1"),
                new CommentRequest("test-id2", "text2"),
                new CommentRequest("test-id3", "text3")
        );
        when(commentRepository.findByText(commentRequests.get(0)))
                .thenReturn(Mono.just(new CommentResponse("test-id1", Map.of("test", 0.0))));
        when(commentRepository.findByText(commentRequests.get(1)))
                .thenReturn(Mono.just(new CommentResponse("test-id2", Map.of("test", 0.0))));
        when(commentRepository.findByText(commentRequests.get(2)))
                .thenReturn(Mono.just(new CommentResponse("test-id3", Map.of("test", 0.0))));
        // expect
        commentService.getCommentTags(commentRequests)
                .as(StepVerifier::create)
                .expectNextCount(3L)
                .verifyComplete();
    }

    @Test
    @DisplayName("캐시에 존재하지 않으면 categorizer 호출 후 캐시에 저장")
    void fallbackTest() {
        // given
        when(commentRepository.findByText(any())).thenReturn(Mono.empty());
        when(commentRepository.save(anyString(), anyMap())).thenReturn(Mono.just(true));
        // when
        commentService.getCommentTags(List.of(new CommentRequest("test-id1", "cache miss")))
                .sequential()
                .blockFirst();
        //then
        verify(commentRepository, times(1)).save(anyString(), anyMap());
    }
}