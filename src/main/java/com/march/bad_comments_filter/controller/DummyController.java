package com.march.bad_comments_filter.controller;

import com.march.bad_comments_filter.service.DummyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class DummyController {
    private final DummyService dummyService;

    @PostMapping("/api/test")
    public CompletableFuture<List> processComment(){
        List<CompletableFuture<String>> futures = IntStream.range(0, 50)
                .mapToObj(i -> CompletableFuture
                        .supplyAsync(()->dummyService.doSomething(String.valueOf(i))))
                .toList();

        // 100개의 댓글을 병렬로 처리 요청
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    @PostMapping(value = "/api/test-async", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> processCommentAsync() {
        return Flux.range(0, 50)
                // flatMap을 사용하여 병렬 처리
                .flatMap(i -> Mono.fromCallable(() -> dummyService.doSomething(String.valueOf(i)))
                        // 각 작업을 별도의 스레드에서 실행
                        .subscribeOn(Schedulers.boundedElastic()))
                // 결과를 순차적으로 처리하지 않고 병렬로 처리
                .parallel()
                .runOn(Schedulers.parallel())
                .sequential();
    }
}
