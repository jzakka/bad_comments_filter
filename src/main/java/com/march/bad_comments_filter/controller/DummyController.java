package com.march.bad_comments_filter.controller;

import com.march.bad_comments_filter.service.DummyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class DummyController {
    private final DummyService dummyService;

    @PostMapping("/api/test")
    public CompletableFuture<List> processComment(){
        List<CompletableFuture<String>> futures = IntStream.range(0, 100)
                .mapToObj(i -> CompletableFuture
                        .supplyAsync(()->dummyService.doSomething(String.valueOf(i))))
                .toList();

        // 100개의 댓글을 병렬로 처리 요청
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }
}
