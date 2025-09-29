package com.example.bankcards.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAllCardsRequestDto {
    private String searchKey;

    private boolean ignoreDeleted;
}
