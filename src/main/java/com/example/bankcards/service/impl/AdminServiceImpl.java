package com.example.bankcards.service.impl;

import static com.example.bankcards.config.SecurityConstants.HAS_ROLE_ADMIN;
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
import com.example.bankcards.exception.ApiException;
import com.example.bankcards.exception.ErrorCode;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AdminService;
import com.example.bankcards.util.CardNumberGeneratorUtils;
import com.example.bankcards.util.specs.CardSearchSpecification;
import com.example.bankcards.util.specs.pojo.CardSearchDetails;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final UserToUserResponseDtoMapper userResponseDtoMapper;
    private final CardResponseDtoMapper cardResponseDtoMapper;

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    @Transactional(readOnly = true)
    public Page<UserToAdminResponseDto> findUsers(UserAdminSearchRequestDto search, Pageable pageable) {
        return userRepository
                .findForAdmin(pageable)
                .map(user -> UserToAdminResponseDto.builder()
                        .ref(user.getRef())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .blocked(user.isBlocked())
                        .build());
    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    @Transactional(readOnly = true)
    public UserToAdminDetailedResponseDto findUserByRef(UUID ref) {
        var user = userRepository.findByRef(ref).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return userResponseDtoMapper.apply(user);
    }

    @Override
    @Transactional
    @PreAuthorize(HAS_ROLE_ADMIN)
    public void updateUser(UUID ref, UserUpdateAdminRequestDto request) {
        var user = userRepository.findByRef(ref)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        user.setBlocked(request.isBlocked());
        userRepository.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize(HAS_ROLE_ADMIN)
    public CardResponseDto createCardForUser(CardCreateRequestDto request) {
        var user = userRepository.findByRef(request.getUserRef())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        String cardNumber;
        do {
            cardNumber = CardNumberGeneratorUtils.generateCardNumber();
        } while (cardRepository.existsByCardNumber(cardNumber));

        String ownerName = StringUtils.join(user.getFirstName().toUpperCase(), " ", user.getLastName().toUpperCase());

        Card card = Card.builder()
                .cardNumber(cardNumber)
                .user(user)
                .cardType(request.getType())
                .balance(new BigDecimal(0))
                .ownerName(ownerName)
                .expirationDate(LocalDate.now().plusYears(4))
                .status(CardStatus.ACTIVE)
                .build();

        cardRepository.save(card);
        log.info("Card for user {} created", user.getRef());

        return cardResponseDtoMapper.apply(card);
    }

    @Override
    @Transactional
    @PreAuthorize(HAS_ROLE_ADMIN)
    public Page<CardResponseDto> getAllCards(CardSearchRequestDto search, Pageable pageable) {
        var specification = new CardSearchSpecification(CardSearchDetails.builder()
                .cardRef(search.getCardRef())
                .cardNumber(search.getCardNumber())
                .ownerRef(search.getOwnerRef())
                .cardType(search.getCardType())
                .cardStatus(search.getCardStatus())
                .build());

        return cardRepository.findAll(specification, pageable)
                .map(card -> CardResponseDto.builder()
                        .ref(card.getRef())
                        .number(card.getCardNumber())
                        .owner(card.getOwnerName())
                        .type(card.getCardType())
                        .expirationDate(card.getExpirationDate())
                        .status(card.getStatus().name())
                        .balance(card.getBalance())
                        .build());
    }

    @Override
    @Transactional
    @PreAuthorize(HAS_ROLE_ADMIN)
    public void blockCard(UUID ref) {
        var card = cardRepository.findByRef(ref).orElseThrow(()-> new ApiException(ErrorCode.CARD_NOT_FOUND));
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    @Transactional
    @PreAuthorize(HAS_ROLE_ADMIN)
    public void activateCard(UUID ref) {
        var card = cardRepository.findByRef(ref)
                .orElseThrow(() -> new ApiException(ErrorCode.CARD_NOT_FOUND));
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    @Override
    @Transactional
    @PreAuthorize(HAS_ROLE_ADMIN)
    public void deleteCard(Long ref) {
        var card = cardRepository.findById(ref)
                .orElseThrow(() -> new ApiException(ErrorCode.CARD_NOT_FOUND));
        cardRepository.delete(card);
    }

}
