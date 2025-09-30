package com.example.bankcards.dto.user.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCardsResponseDto {

    private UUID ref;

    private String number;

    private String type;

    private String owner;

    private LocalDate expirationDate;

    private String status;

    private BigDecimal balance;
}
