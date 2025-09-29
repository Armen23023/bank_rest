package com.example.bankcards.dto.signup.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class SignupRequestDto implements Serializable {

    @NotBlank(message = "firstName.required")
    private String firstName;

    private String lastName;

    @NotBlank(message = "email.required")
    private String email;

    @NotBlank(message = "password.required")
    private String password;
}
