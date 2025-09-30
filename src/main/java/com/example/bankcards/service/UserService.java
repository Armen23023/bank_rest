package com.example.bankcards.service;

import com.example.bankcards.dto.user.request.CardTransactionRequestDto;
import com.example.bankcards.dto.user.request.UserAllCardsRequestDto;
import com.example.bankcards.dto.user.response.CardTransactionResponseDto;
import com.example.bankcards.dto.user.response.UserCardsResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    /**
     * Get a paginated list of cards belonging to the currently authenticated user.
     * @param pageable pagination information.
     * @return paged {@link UserCardsResponseDto} representing the user's cards.
     */
    Page<UserCardsResponseDto> getMyCards(UserAllCardsRequestDto request, Pageable pageable);

    /**
     * Retrieve detailed information about a specific card by its unique reference.
     * @param ref unique identifier of the card.
     * @return  the {@link UserCardsResponseDto} if found,
     */

    UserCardsResponseDto getCardByRef(UUID ref);

    /**
     * Retrieve the balance of a specific card by its unique reference.
     * @param ref unique identifier of the card.
     * @return the card balance as a string,
     */
    String getCardBalance(UUID ref);

    CardTransactionResponseDto transferBetweenCards(CardTransactionRequestDto request);

    void requestCardBlock(UUID ref);
}
