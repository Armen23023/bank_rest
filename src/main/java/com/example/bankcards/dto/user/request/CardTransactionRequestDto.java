package com.example.bankcards.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CardTransactionRequestDto")
public class CardTransactionRequestDto {

    @NotNull(message = "Output card reference is required")
    @Schema(description = "Reference of the card to transfer money from",
            example = "75630641-f3e5-4a85-a778-fe8443cc039f")
    private UUID outputCardRef;

    @NotNull(message = "Input card reference is required")
    @Schema(description = "Reference of the card to transfer money to",
            example = "12345678-abcd-4e56-9876-fe8443cc9876")
    private UUID inputCardRef;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Schema(description = "Amount to transfer", example = "150.75")
    private BigDecimal amount;
}
