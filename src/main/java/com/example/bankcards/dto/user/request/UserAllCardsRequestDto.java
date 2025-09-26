package com.example.bankcards.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserAllCardsRequest")
public class UserAllCardsRequestDto {
    @Schema(description = "Search keyword")
    private String searchKey;

    @Schema(description = "Ignore deleted archives")
    private boolean ignoreDeleted;
}
