package com.example.bankcards.service;

import com.example.bankcards.dto.user.request.CardTransactionRequestDto;
import com.example.bankcards.dto.user.request.UserAllCardsRequestDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ApiException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.UserServiceImpl;
import com.example.bankcards.util.CardMaskUtils;
import com.example.bankcards.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Card card;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");

        card = new Card();
        card.setRef(UUID.randomUUID());
        card.setUser(user);
        card.setCardNumber("1234567812345678");
        card.setCardType("VISA");
        card.setOwnerName("Armen Mirzoyan");
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.valueOf(100));
        card.setExpirationDate(LocalDate.now().plusYears(2));
    }

    @Test
    void getMyCards_shouldReturnCards() {
        try (var mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn("test@example.com");

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
            when(cardRepository.findAllByUser(eq(user), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(card)));

            var result = userService.getMyCards(new UserAllCardsRequestDto(), PageRequest.of(0, 10));

            assertThat(result).hasSize(1);
            assertThat(result.getContent().get(0).getOwner()).isEqualTo("Armen Mirzoyan");
        }
    }

    @Test
    void getMyCards_shouldThrowIfUserNotFound() {
        try (var mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn(null);

            assertThrows(ApiException.class, () ->
                    userService.getMyCards(new UserAllCardsRequestDto(), PageRequest.of(0, 10))
            );
        }
    }

    @Test
    void getCardByRef_shouldReturnCard() {
        try (var mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn("test@example.com");

            when(cardRepository.findByRef(card.getRef())).thenReturn(Optional.of(card));

            var result = userService.getCardByRef(card.getRef());

            assertThat(CardMaskUtils.maskCardNumber(result.getNumber()).equals("1234567812345678")); // masked
            assertThat(result.getStatus()).isEqualTo("ACTIVE");
        }
    }

    @Test
    void getCardByRef_shouldThrowIfBlocked() {
        try (var mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn("test@example.com");
            card.setStatus(CardStatus.BLOCKED);

            when(cardRepository.findByRef(card.getRef())).thenReturn(Optional.of(card));

            assertThrows(ApiException.class, () -> userService.getCardByRef(card.getRef()));
        }
    }

    @Test
    void getCardBalance_shouldReturnBalance() {
        try (var mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn("test@example.com");

            when(cardRepository.findByRef(card.getRef())).thenReturn(Optional.of(card));

            var balance = userService.getCardBalance(card.getRef());

            assertThat(balance).isEqualTo("100");
        }
    }

    @Test
    void transferBetweenCards_shouldTransferMoney() {
        try (var mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn("test@example.com");

            Card targetCard = new Card();
            targetCard.setRef(UUID.randomUUID());
            targetCard.setUser(user);
            targetCard.setStatus(CardStatus.ACTIVE);
            targetCard.setBalance(BigDecimal.valueOf(50));

            var request = new CardTransactionRequestDto(
                    card.getRef(), targetCard.getRef(), BigDecimal.valueOf(40)
            );

            when(cardRepository.findByRef(card.getRef())).thenReturn(Optional.of(card));
            when(cardRepository.findByRef(targetCard.getRef())).thenReturn(Optional.of(targetCard));

            var response = userService.transferBetweenCards(request);

            assertThat(response.getAmount()).isEqualTo(BigDecimal.valueOf(40));
            assertThat(card.getBalance()).isEqualTo(BigDecimal.valueOf(60));
            assertThat(targetCard.getBalance()).isEqualTo(BigDecimal.valueOf(90));

            verify(cardRepository, times(2)).save(any(Card.class));
        }
    }

    @Test
    void requestCardBlock_shouldSetPending() {
        try (var mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn("test@example.com");

            when(cardRepository.findByRef(card.getRef())).thenReturn(Optional.of(card));

            userService.requestCardBlock(card.getRef());

            assertThat(card.getStatus()).isEqualTo(CardStatus.PENDING);
            verify(cardRepository).save(card);
        }
    }
}
