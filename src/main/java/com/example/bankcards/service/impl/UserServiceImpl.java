package com.example.bankcards.service.impl;

import com.example.bankcards.dto.user.request.CardTransactionRequestDto;
import com.example.bankcards.dto.user.request.UserAllCardsRequestDto;
import com.example.bankcards.dto.user.response.CardTransactionResponseDto;
import com.example.bankcards.dto.user.response.UserCardsResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ApiException;
import com.example.bankcards.exception.ErrorCode;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.CardMaskUtils;
import com.example.bankcards.util.SecurityUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UserCardsResponseDto> getMyCards(UserAllCardsRequestDto request, Pageable pageable) {
        var email = SecurityUtils.getCurrentUsername();

        if (email == null) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        return cardRepository.findAllByUser(currentUser, pageable)
                .map(card -> UserCardsResponseDto.builder()
                        .ref(card.getRef())
                        .number(CardMaskUtils.maskCardNumber(card.getCardNumber()))
                        .type(card.getCardType())
                        .owner(card.getOwnerName())
                        .status(card.getStatus().name())
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public UserCardsResponseDto getCardByRef(UUID ref) {
        String currentUserEmail = SecurityUtils.getCurrentUsername();

        Card card = cardRepository.findByRef(ref)
                .orElseThrow(() -> new ApiException(ErrorCode.CARD_NOT_FOUND));

        // Security check
        if (!card.getUser().getEmail().equals(currentUserEmail)) {
            throw new ApiException(ErrorCode.ACCESS_DENIED);
        }

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new ApiException(ErrorCode.CARD_BLOCKED);
        }

        if (card.getStatus() == CardStatus.EXPIRED) {
            throw new ApiException(ErrorCode.CARD_EXPIRED);
        }

        return UserCardsResponseDto.builder()
                .ref(card.getRef())
                .number(CardMaskUtils.maskCardNumber(card.getCardNumber()))
                .type(card.getCardType())
                .status(card.getStatus().name())
                .expirationDate(card.getExpirationDate())
                .balance(card.getBalance())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public String getCardBalance(UUID ref) {
        String currentUserEmail = SecurityUtils.getCurrentUsername();

        Card card = cardRepository.findByRef(ref)
                .orElseThrow(() -> new ApiException(ErrorCode.CARD_NOT_FOUND));

        // Security check
        if (!card.getUser().getEmail().equals(currentUserEmail)) {
            throw new ApiException(ErrorCode.ACCESS_DENIED);
        }

        return card.getBalance().toString();
    }

    @Override
    @Transactional
    public CardTransactionResponseDto transferBetweenCards(CardTransactionRequestDto request) {
        String currentUser = SecurityUtils.getCurrentUsername();

        var sourceCard = cardRepository.findByRef(request.getFromCardRef())
                .orElseThrow(() -> new ApiException(ErrorCode.CARD_NOT_FOUND));

        var targetCard = cardRepository.findByRef(request.getToCardRef())
                .orElseThrow(() -> new ApiException(ErrorCode.CARD_NOT_FOUND));

        if ( !sourceCard.getUser().getEmail().equals(currentUser)
                && ! targetCard.getUser().getEmail().equals(currentUser)) {
            throw new ApiException(ErrorCode.CARD_NOT_OWNED_BY_USER);
        }

        if (sourceCard.getStatus() == CardStatus.BLOCKED || targetCard.getStatus() == CardStatus.BLOCKED) {
            throw new ApiException(ErrorCode.CARD_BLOCKED);
        }

        if (sourceCard.getStatus() == CardStatus.EXPIRED || targetCard.getStatus() == CardStatus.EXPIRED) {
            throw new ApiException(ErrorCode.CARD_EXPIRED);
        }

        if (sourceCard.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ApiException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        sourceCard.setBalance(sourceCard.getBalance().subtract(request.getAmount()));
        targetCard.setBalance(targetCard.getBalance().add(request.getAmount()));

        cardRepository.save(sourceCard);
        cardRepository.save(targetCard);

        return new CardTransactionResponseDto(
                targetCard.getBalance(),
                sourceCard.getBalance(),
                request.getAmount(),
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional
    public void requestCardBlock(UUID ref) {
        String currentUserEmail = SecurityUtils.getCurrentUsername();

        var card = cardRepository.findByRef(ref)
                .orElseThrow(() -> new ApiException(ErrorCode.CARD_NOT_FOUND));

        if (!card.getUser().getEmail().equals(currentUserEmail)) {
            throw new ApiException(ErrorCode.CARD_NOT_OWNED_BY_USER);
        }

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new ApiException(ErrorCode.CARD_BLOCKED);
        }

        card.setStatus(CardStatus.PENDING);
        cardRepository.save(card);
    }
}
