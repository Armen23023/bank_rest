package com.example.bankcards.dto.admin.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardSearchRequestDto implements Serializable {
    private String cardRef;

    private String cardStatus;

    private String ownerRef;

    private String cardType;

    private String cardNumber;
}
