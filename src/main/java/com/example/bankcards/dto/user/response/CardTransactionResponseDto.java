package com.example.bankcards.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CardTransactionResponseDto")
public class CardTransactionResponseDto {

    @Schema(description = "Balance of the target card after transaction", example = "1200.50")
    private BigDecimal targetCardBalance;

    @Schema(description = "Balance of the source card after transaction", example = "350.75")
    private BigDecimal sourceCardBalance;


    @Schema(description = "Transferred amount", example = "150.00")
    private BigDecimal amount;

    @Schema(description = "Transaction timestamp", example = "2025-09-26T15:30:00")
    private LocalDateTime timestamp;
}
