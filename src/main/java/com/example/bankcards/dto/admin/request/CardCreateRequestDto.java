package com.example.bankcards.dto.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCreateRequestDto implements Serializable {

    @Schema(description = "User ref", example = "75630641-f3e5-4a85-a778-fe8443cc039f")
    private UUID userRef;

    @Schema(description = "Card type", example = "VISA")
    private String type;
}
