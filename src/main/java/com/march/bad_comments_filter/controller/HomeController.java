package com.march.bad_comments_filter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/ping")
    public ResponseEntity ping() {
        return ResponseEntity.ok().build();
    }
}
