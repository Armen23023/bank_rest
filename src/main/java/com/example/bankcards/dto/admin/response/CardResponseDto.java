package com.example.bankcards.dto.admin.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponseDto implements Serializable {

    private UUID ref;

    private String number;

    private String type;

    private String owner;

    private LocalDate expirationDate;

    private String status;

    private BigDecimal balance;
}
