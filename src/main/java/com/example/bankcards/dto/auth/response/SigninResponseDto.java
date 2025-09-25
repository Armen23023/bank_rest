package com.example.bankcards.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninResponseDto {
    @Schema(name = "accessToken", description = "The access token that needs to be used on the service to access APIs")
    private String accessToken;
    @Schema(
            name = "refreshToken",
            description = "The refresh token that needs to be used to refresh access token",
            hidden = true)
    private String refreshToken;
}
