package com.example.bankcards.dto.auth.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

@Data
@Schema(name = "RefreshTokenRequestDto")
public class RefreshTokenRequestDto implements Serializable {
    @NotBlank(message = "refresh token.required")
    @Schema(description = "refresh token required for signin.")
    private String refreshToken;
}
