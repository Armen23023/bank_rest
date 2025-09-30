package com.example.bankcards.util.specs.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardSearchDetails {
    private String cardRef;
    private String ownerRef;
    private String cardType;
    private String cardNumber;
    private String cardStatus;
}
