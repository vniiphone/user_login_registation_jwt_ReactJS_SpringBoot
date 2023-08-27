package com.example.demo_jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo_jwt.payloads.responses.MessageResponse;

@RestController
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<MessageResponse> test() {
        return ResponseEntity.ok(new MessageResponse("This is a test controller for Authentication"));
    }
}
