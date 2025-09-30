package com.example.bankcards.controller;

import com.example.bankcards.dto.user.request.CardTransactionRequestDto;
import com.example.bankcards.dto.user.request.UserAllCardsRequestDto;
import com.example.bankcards.dto.user.response.CardTransactionResponseDto;
import com.example.bankcards.dto.user.response.UserCardsResponseDto;
import com.example.bankcards.security.jwt.service.JwtService;
import com.example.bankcards.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void getMyCards() throws Exception {
        UserCardsResponseDto cardDto = UserCardsResponseDto.builder()
                .ref(UUID.randomUUID())
                .number("1234-5678-9012-3456")
                .balance(BigDecimal.valueOf(500))
                .build();

        Page<UserCardsResponseDto> page = new PageImpl<>(List.of(cardDto));

        when(userService.getMyCards(any(UserAllCardsRequestDto.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/users/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].number").value("1234-5678-9012-3456"));
    }

    @Test
    void getCardDetails() throws Exception {
        UUID ref = UUID.randomUUID();
        UserCardsResponseDto cardDto = UserCardsResponseDto.builder()
                .ref(ref)
                .number("1234-5678-9012-3456")
                .balance(BigDecimal.valueOf(500))
                .build();

        when(userService.getCardByRef(ref)).thenReturn(cardDto);

        mockMvc.perform(get("/api/v1/users/cards/{ref}", ref))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ref").value(ref.toString()))
                .andExpect(jsonPath("$.number").value("1234-5678-9012-3456"));
    }

    @Test
    void requestCardBlock() throws Exception {
        UUID ref = UUID.randomUUID();
        doNothing().when(userService).requestCardBlock(ref);

        mockMvc.perform(post("/api/v1/users/cards/{ref}/block-request", ref))
                .andExpect(status().isOk())
                .andExpect(content().string("Block request submitted for card: " + ref));

        verify(userService, times(1)).requestCardBlock(ref);
    }

    @Test
    void transferBetweenCards() throws Exception {
        CardTransactionRequestDto requestDto = CardTransactionRequestDto.builder()
                .fromCardRef(UUID.randomUUID())
                .toCardRef(UUID.randomUUID())
                .amount(BigDecimal.valueOf(500))
                .build();

        CardTransactionResponseDto responseDto = CardTransactionResponseDto.builder()
                .sourceCardBalance(BigDecimal.ZERO)
                .targetCardBalance(BigDecimal.ZERO)
                .amount(BigDecimal.valueOf(500))
                .timestamp(LocalDateTime.of(1,1,1,1,1))
                .build();

        when(userService.transferBetweenCards(any(CardTransactionRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/users/cards/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sourceCardBalance").value(responseDto.getSourceCardBalance()))
                .andExpect(jsonPath("$.targetCardBalance").value(responseDto.getTargetCardBalance()))
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    void getCardBalance() throws Exception {
        UUID ref = UUID.randomUUID();
        when(userService.getCardBalance(ref)).thenReturn("1000");

        mockMvc.perform(get("/api/v1/users/cards/{ref}/balance", ref))
                .andExpect(status().isOk())
                .andExpect(content().string("1000"));
    }
}