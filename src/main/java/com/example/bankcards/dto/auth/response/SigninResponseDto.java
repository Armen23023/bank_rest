package com.example.bankcards.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninResponseDto {
    private String accessToken;

    private String refreshToken;
}
