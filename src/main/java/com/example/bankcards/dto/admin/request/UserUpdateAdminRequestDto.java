package com.example.bankcards.dto.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserUpdateAdminRequestDto")
public class UserUpdateAdminRequestDto implements Serializable {

    @Schema(description = "Blocked", example = "true")
    private boolean blocked;
}
