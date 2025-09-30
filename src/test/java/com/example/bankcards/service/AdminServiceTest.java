package com.example.bankcards.service;

import com.example.bankcards.dto.admin.request.CardCreateRequestDto;
import com.example.bankcards.dto.admin.request.CardSearchRequestDto;
import com.example.bankcards.dto.admin.request.UserAdminSearchRequestDto;
import com.example.bankcards.dto.admin.request.UserUpdateAdminRequestDto;
import com.example.bankcards.dto.admin.response.CardResponseDto;
import com.example.bankcards.dto.admin.response.UserToAdminDetailedResponseDto;
import com.example.bankcards.dto.admin.response.UserToAdminResponseDto;
import com.example.bankcards.dto.mapper.CardResponseDtoMapper;
import com.example.bankcards.dto.mapper.UserToUserResponseDtoMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ApiException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.AdminServiceImpl;
import com.example.bankcards.util.specs.CardSearchSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserToUserResponseDtoMapper userResponseDtoMapper;

    @Mock
    private CardResponseDtoMapper cardResponseDtoMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void findUsers_shouldReturnPageOfUserDtos() {
        UserAdminSearchRequestDto search = new UserAdminSearchRequestDto();
        Pageable pageable = Pageable.unpaged();

        User user = new User();
        user.setRef(UUID.randomUUID());
        user.setFirstName("Armen");
        user.setLastName("Mirzoyan");
        user.setEmail("armen@example.com");
        user.setBlocked(false);

        Page<User> userPage = new PageImpl<>(List.of(user));

        when(userRepository.findForAdmin(pageable)).thenReturn(userPage);

        Page<UserToAdminResponseDto> result = adminService.findUsers(search, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Armen", result.getContent().getFirst().getFirstName());
    }

    @Test
    void findUserByRef_existingUser_shouldReturnDetailedDto() {
        UUID ref = UUID.randomUUID();
        User user = new User();
        when(userRepository.findByRef(ref)).thenReturn(Optional.of(user));

        UserToAdminDetailedResponseDto dto = new UserToAdminDetailedResponseDto();
        when(userResponseDtoMapper.apply(user)).thenReturn(dto);

        UserToAdminDetailedResponseDto result = adminService.findUserByRef(ref);

        assertNotNull(result);
        verify(userRepository).findByRef(ref);
        verify(userResponseDtoMapper).apply(user);
    }

    @Test
    void findUserByRef_nonExistingUser_shouldThrow() {
        UUID ref = UUID.randomUUID();
        when(userRepository.findByRef(ref)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> adminService.findUserByRef(ref));
    }

    @Test
    void updateUser_shouldSetBlockedAndSave() {
        UUID ref = UUID.randomUUID();
        User user = new User();
        user.setBlocked(false);

        when(userRepository.findByRef(ref)).thenReturn(Optional.of(user));

        UserUpdateAdminRequestDto request = new UserUpdateAdminRequestDto();
        request.setBlocked(true);

        adminService.updateUser(ref, request);

        assertTrue(user.isBlocked());
        verify(userRepository).save(user);
    }

    @Test
    void createCardForUser_shouldCreateCard() {
        UUID userRef = UUID.randomUUID();
        User user = new User();
        user.setRef(userRef);
        user.setFirstName("Armen");
        user.setLastName("Mirzoyan");

        when(userRepository.findByRef(userRef)).thenReturn(Optional.of(user));
        when(cardRepository.existsByCardNumber(anyString())).thenReturn(false);

        when(cardResponseDtoMapper.apply(any(Card.class))).thenReturn(CardResponseDto.builder()
                        .owner("ARMEN MIRZOYAN")
                        .type("VISA")
                .build());

        CardCreateRequestDto request = new CardCreateRequestDto();
        request.setUserRef(userRef);
        request.setType("VISA");

        CardResponseDto result = adminService.createCardForUser(request);

        assertEquals("VISA", result.getType());
        assertEquals("ARMEN MIRZOYAN",result.getOwner());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void blockCard_shouldSetStatusBlocked() {
        UUID ref = UUID.randomUUID();
        Card card = Card.builder().status(CardStatus.ACTIVE).build();
        when(cardRepository.findByRef(ref)).thenReturn(Optional.of(card));

        adminService.blockCard(ref);

        assertEquals(CardStatus.BLOCKED, card.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void activateCard_shouldSetStatusActive() {
        UUID ref = UUID.randomUUID();
        Card card = Card.builder().status(CardStatus.BLOCKED).build();
        when(cardRepository.findByRef(ref)).thenReturn(Optional.of(card));

        adminService.activateCard(ref);

        assertEquals(CardStatus.ACTIVE, card.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void deleteCard_shouldCallDelete() {
        Long ref = 1L;
        Card card = Card.builder().build();
        when(cardRepository.findById(ref)).thenReturn(Optional.of(card));

        adminService.deleteCard(ref);

        verify(cardRepository).delete(card);
    }

    @Test
    void getAllCards_shouldReturnPage() {
        CardSearchRequestDto search = new CardSearchRequestDto();
        Pageable pageable = Pageable.unpaged();

        Card card = Card.builder()
                .cardNumber("1234")
                .ownerName("Armen Mirzoyan")
                .balance(BigDecimal.TEN)
                .status(CardStatus.ACTIVE)
                .build();

        Page<Card> page = new PageImpl<>(List.of(card));

        when(cardRepository.findAll(any(CardSearchSpecification.class), eq(pageable)))
                .thenReturn(page);


        Page<CardResponseDto> result = adminService.getAllCards(search, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("1234", result.getContent().getFirst().getNumber());
        assertEquals("Armen Mirzoyan", result.getContent().getFirst().getOwner());
    }
}
