package com.example.bankcards.dto.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "CardResponseDto")
public class CardResponseDto implements Serializable {

    @Schema(description = "Card reference")
    private UUID ref;

    @Schema(description = "Card number")
    private String number;

    @Schema(description = "Card type",example = "VISA")
    private String type;

    @Schema(description = "Card owner name",example = "ARMEN MIRZOYAN")
    private String owner;

    @Schema(description = "Expiration date",example = "9/26/2028")
    private LocalDate expirationDate;

    @Schema(description = "Card Status",example = "ACTIVE")
    private String status;

    @Schema(description = "Card balance",example = "150.7")
    private BigDecimal balance;
}
