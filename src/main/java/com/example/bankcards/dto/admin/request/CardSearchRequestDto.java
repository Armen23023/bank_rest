package com.example.bankcards.dto.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CardSearchRequestDto")
public class CardSearchRequestDto implements Serializable {
    @Schema(description = "Card ref")
    private String cardRef;

    @Schema(description = "Card status",example = "BLOCKED")
    private String cardStatus;

    @Schema(description = "User ref")
    private String ownerRef;

    @Schema(description = "Card type",example = "VISA")
    private String cardType;

    @Schema(description = "Card number",example = "4000000202939460")
    private String cardNumber;
}
