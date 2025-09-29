package com.example.bankcards.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class SigninRequestDto implements Serializable {

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;
}
