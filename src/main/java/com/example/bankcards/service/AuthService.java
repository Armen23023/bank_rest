package com.example.bankcards.service;

import com.example.bankcards.dto.auth.request.RefreshTokenRequestDto;
import com.example.bankcards.dto.auth.request.SigninRequestDto;
import com.example.bankcards.dto.auth.response.SigninResponseDto;

public interface AuthService {

    /**
     * Authenticate user and generate JWT tokens.
     * @param requestDto {@link SigninRequestDto} containing login credentials.
     * @return {@link SigninResponseDto} containing access and refresh tokens.
     */
    SigninResponseDto login(SigninRequestDto requestDto);

    /**
     * Refresh access token using a valid refresh token.
     */
    SigninResponseDto refreshToken(RefreshTokenRequestDto request);
}
