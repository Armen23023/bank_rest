package com.example.bankcards.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

@Data
public class RefreshTokenRequestDto implements Serializable {
    @NotBlank(message = "refresh token.required")
    private String refreshToken;
}
