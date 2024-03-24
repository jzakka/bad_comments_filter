package com.march.bad_comments_filter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class DummyService {
    Random random = new Random();
    public String doSomething(String origin){
        process();
        return origin + "processed";
    }

    private String process(){
        // 댓글 하나당 처리에 최대 200ms 소요된다고 가정
        int nlpProcessingTime = random.nextInt(200);
//        log.info("process time={}", nlpProcessingTime);
        try{
            Thread.sleep(nlpProcessingTime);
        }catch (InterruptedException e) {
            throw new RuntimeException("Exception occured!", e);
        }
        return "completed";
    }
}
