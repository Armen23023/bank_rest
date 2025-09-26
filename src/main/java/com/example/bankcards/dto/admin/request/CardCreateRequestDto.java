package com.example.bankcards.dto.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CardCreateRequestDto")
public class CardCreateRequestDto implements Serializable {

    @NotNull(message = "User ref is required")
    @Schema(description = "User ref", example = "75630641-f3e5-4a85-a778-fe8443cc039f")
    private UUID userRef;

    @NotBlank(message = "Card type is required")
    @Schema(description = "Card type", example = "VISA")
    private String type;
}
