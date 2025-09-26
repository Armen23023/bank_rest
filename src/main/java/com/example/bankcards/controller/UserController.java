package com.example.bankcards.controller;

import com.example.bankcards.dto.user.request.CardTransactionRequestDto;
import com.example.bankcards.dto.user.request.UserAllCardsRequestDto;
import com.example.bankcards.dto.user.response.CardTransactionResponseDto;
import com.example.bankcards.dto.user.response.UserCardsResponseDto;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/cards")
    public Page<UserCardsResponseDto> getMyCards(
            final UserAllCardsRequestDto request,@PageableDefault @Parameter(hidden = true) Pageable pageable) {
            log.info("Fetching current user cards");
         return userService.getMyCards(request,pageable);
    }

    @GetMapping("/cards/{ref}")
    public ResponseEntity<UserCardsResponseDto> getCardDetails(
            @PathVariable(value = "ref") final UUID ref) {
        var response =  userService.getCardByRef(ref);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cards/{ref}/block-request")
    public ResponseEntity<?> requestCardBlock(@PathVariable UUID ref) {
        userService.requestCardBlock(ref);
        return ResponseEntity.ok("Block request submitted for card: " + ref);
    }

    @PostMapping("/cards/transfer")
    public ResponseEntity<CardTransactionResponseDto> transferBetweenCards(
         @RequestBody @Validated final CardTransactionRequestDto request) {
        var response =  userService.transferBetweenCards(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cards/{ref}/balance")
    public ResponseEntity<String> getCardBalance(@PathVariable UUID ref) {
        String cardBalance = userService.getCardBalance(ref);
        return ResponseEntity.ok(cardBalance);
    }

}
