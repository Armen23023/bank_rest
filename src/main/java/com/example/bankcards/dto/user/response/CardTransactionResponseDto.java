package com.example.bankcards.dto.user.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTransactionResponseDto {

    private BigDecimal targetCardBalance;

    private BigDecimal sourceCardBalance;

    private BigDecimal amount;

    private LocalDateTime timestamp;
}
