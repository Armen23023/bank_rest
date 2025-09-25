package com.example.bankcards.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
@Schema(name = "SigninRequestDto")
public class SigninRequestDto implements Serializable {

    @NotBlank(message = "username is required")
    @Schema(description = "username", example = "joh.doe")
    private String username;

    @NotBlank(message = "password is required")
    @Schema(description = "password", example = "StrongPassw@rd!@#")
    private String password;
}
