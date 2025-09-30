package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.admin.response.CardResponseDto;
import com.example.bankcards.entity.Card;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardResponseDtoMapper implements Function<Card,CardResponseDto > {
    @Override
    public CardResponseDto apply(Card card) {
        CardResponseDto cardResponseDto = new CardResponseDto();
        cardResponseDto.setRef(card.getRef());
        cardResponseDto.setOwner(card.getOwnerName());
        cardResponseDto.setBalance(card.getBalance());
        cardResponseDto.setType(card.getCardType());
        cardResponseDto.setNumber(card.getCardNumber());
        cardResponseDto.setStatus(String.valueOf(card.getStatus()));
        cardResponseDto.setExpirationDate(card.getExpirationDate());
        return cardResponseDto;
    }
}
