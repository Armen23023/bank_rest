package com.example.bankcards.dto.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserAdminSearchRequestDto")
public class UserAdminSearchRequestDto implements Serializable {
    @Schema(description = "Include blocked in the response", example = "true")
    private boolean includeBlocked;
}
