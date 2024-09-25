package com.epam.wca.gym.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @GetMapping
    public ResponseEntity<String> getSimpleData() {
        return ResponseEntity.ok("Test endpoint hit");
    }
}
