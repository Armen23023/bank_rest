package com.example.bankcards.controller;

import com.example.bankcards.dto.auth.request.RefreshTokenRequestDto;
import com.example.bankcards.dto.auth.request.SigninRequestDto;
import com.example.bankcards.dto.auth.response.SigninResponseDto;
import com.example.bankcards.service.AuthService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/signin")
    public ResponseEntity<SigninResponseDto> signin(@RequestBody @Validated final SigninRequestDto request) {
        log.info("Signing in using credentials: {}", request);
        SigninResponseDto response = authService.login(request);
        log.info("Successfully authenticated with credentials: {}", request);
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/refresh")
    public ResponseEntity<SigninResponseDto> refreshToken(@RequestBody @Validated final RefreshTokenRequestDto request) {
        log.info("Refreshing token: {}", request);
        SigninResponseDto responseDto = authService.refreshToken(request);
        log.info("Successfully refreshed token: {}", request);
        return ResponseEntity.ok(responseDto);
    }

}

