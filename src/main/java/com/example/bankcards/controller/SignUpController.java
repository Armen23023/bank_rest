package com.example.bankcards.controller;

import com.example.bankcards.dto.signup.request.SignupRequestDto;
import com.example.bankcards.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final SignupService signupService;

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody @Validated final SignupRequestDto request) {
        log.info("Trying to signup with details: {}", request);
        signupService.signup(request);
        return ResponseEntity.ok("User registered successfully");
    }
}
