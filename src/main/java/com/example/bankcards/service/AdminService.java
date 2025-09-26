package com.example.bankcards.service;

import com.example.bankcards.dto.admin.request.CardCreateRequestDto;
import com.example.bankcards.dto.admin.request.CardSearchRequestDto;
import com.example.bankcards.dto.admin.request.UserAdminSearchRequestDto;
import com.example.bankcards.dto.admin.request.UserUpdateAdminRequestDto;
import com.example.bankcards.dto.admin.response.CardResponseDto;
import com.example.bankcards.dto.admin.response.UserToAdminDetailedResponseDto;
import com.example.bankcards.dto.admin.response.UserToAdminResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    /**
     * Find users.
     * @param pageable page details.
     * @return paged {@link UserToAdminResponseDto}
     */
    Page<UserToAdminResponseDto> findUsers(UserAdminSearchRequestDto search, Pageable pageable);

    /**
     * Find a user by their unique reference.
     * @param ref unique identifier of the user.
     * @return detailed {@link UserToAdminDetailedResponseDto} for admin view.
     */
    UserToAdminDetailedResponseDto findUserByRef(UUID ref);

    /**
     * Update user status by their unique reference.
     * @param ref unique identifier of the user.
     * @param request update details from {@link UserUpdateAdminRequestDto}.
     */
    void updateUser(UUID ref, UserUpdateAdminRequestDto request);

    /**
     * Create a new card for a specific user.
     * @param request card creation details from {@link CardCreateRequestDto}.
     */
    CardResponseDto createCardForUser(CardCreateRequestDto request);

    /**
     * Get a paginated list of cards based on search criteria.
     * @param request search/filter criteria from {@link CardSearchRequestDto}.
     * @param pageable pagination information.
     * @return paged {@link CardResponseDto}.
     */
    Page<CardResponseDto> getAllCards(CardSearchRequestDto request, Pageable pageable);

    /**
     * Block a card by its unique reference.
     * @param ref unique identifier of the card to block.
     */
    void blockCard(UUID ref);

    /**
     * Activate a previously blocked card by its unique reference.
     * @param ref unique identifier of the card to activate.
     */
    void activateCard(UUID ref);

    /**
     * Delete a card by its database ID.
     * @param ref database identifier of the card to delete.
     */
    void deleteCard(Long ref);
}
