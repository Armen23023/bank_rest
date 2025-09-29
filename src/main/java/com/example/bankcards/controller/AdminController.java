package com.example.bankcards.controller;

import com.example.bankcards.dto.admin.request.CardCreateRequestDto;
import com.example.bankcards.dto.admin.request.CardSearchRequestDto;
import com.example.bankcards.dto.admin.request.UserAdminSearchRequestDto;
import com.example.bankcards.dto.admin.request.UserUpdateAdminRequestDto;
import com.example.bankcards.dto.admin.response.CardResponseDto;
import com.example.bankcards.dto.admin.response.UserToAdminDetailedResponseDto;
import com.example.bankcards.dto.admin.response.UserToAdminResponseDto;
import com.example.bankcards.service.AdminService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin")
public class AdminController {

     private final AdminService adminService;


    @GetMapping(value = "/users")
    public Page<UserToAdminResponseDto> findUsers(
            final UserAdminSearchRequestDto search,
            @PageableDefault @Parameter(hidden = true) final Pageable pageable) {
        log.info("trying to get all users");
        return adminService.findUsers(search, pageable);
    }

    @GetMapping(value = "/users/{ref}")
    public ResponseEntity<UserToAdminDetailedResponseDto> findUserByRef(
            @PathVariable(value = "ref") final UUID ref) {
        return ResponseEntity.ok(adminService.findUserByRef(ref));
    }

    @PutMapping("/users/{ref}")
    public ResponseEntity<?> updateUser(
            @RequestBody final UserUpdateAdminRequestDto request,
            @PathVariable(value = "ref") final UUID ref) {
        adminService.updateUser(ref,request);
        return ResponseEntity.ok("User status updated by admin." + ref);
    }

    // ----- Card -----

    @GetMapping("/cards")
    public Page<CardResponseDto> getAllCards(
            final CardSearchRequestDto request, @PageableDefault @Parameter(hidden = true) final Pageable pageable) {
        log.info("Fetching card list with search details: {} and page details: {}", request, pageable);
        return adminService.getAllCards(request,pageable);
    }

    @PostMapping("/cards")
    public ResponseEntity<CardResponseDto> createCard(@Valid @RequestBody CardCreateRequestDto request) {
        return ResponseEntity.ok(adminService.createCardForUser(request));
    }

    @PutMapping("/cards/{ref}/block")
    public ResponseEntity<?> blockCard(@PathVariable(value = "ref") final UUID ref) {
        adminService.blockCard(ref);
        return ResponseEntity.ok("Card blocked by admin. Card ref : " + ref);
    }

    @PutMapping("/cards/{ref}/activate")
    public ResponseEntity<?> activateCard(@PathVariable(value = "ref") final UUID ref) {
        adminService.activateCard(ref);
        return ResponseEntity.ok("Card activated by admin. Card ref : " + ref);
    }

    @DeleteMapping("/cards/{ref}")
    public ResponseEntity<?> deleteCard(@PathVariable Long ref) {
        adminService.deleteCard(ref);
        return ResponseEntity.ok("Card deleted by admin. Card ref : " + ref);
    }
}
