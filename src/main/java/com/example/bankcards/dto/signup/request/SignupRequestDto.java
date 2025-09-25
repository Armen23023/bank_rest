package com.example.bankcards.dto.signup.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
@Schema(name = "SignupRequestDto")
public class SignupRequestDto implements Serializable {

    @NotBlank(message = "firstName.required")
    @Schema(description = "First Name", example = "John")
    private String firstName;

    @Schema(description = "Last Name", example = "Doe")
    private String lastName;

    @NotBlank(message = "email.required")
    @Schema(description = "Email address", example = "john.doe@gmail.com")
    private String email;

    @NotBlank(message = "password.required")
    @Schema(description = "Password", example = "StrongPassw@rd!@#")
    private String password;
}
