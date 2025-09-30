package com.example.bankcards.controller;

import com.example.bankcards.dto.admin.request.CardCreateRequestDto;
import com.example.bankcards.dto.admin.request.CardSearchRequestDto;
import com.example.bankcards.dto.admin.request.UserAdminSearchRequestDto;
import com.example.bankcards.dto.admin.request.UserUpdateAdminRequestDto;
import com.example.bankcards.dto.admin.response.CardResponseDto;
import com.example.bankcards.dto.admin.response.UserToAdminDetailedResponseDto;
import com.example.bankcards.dto.admin.response.UserToAdminResponseDto;
import com.example.bankcards.security.jwt.service.JwtService;
import com.example.bankcards.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private JwtService jwtService;


    @Test
    void findUsers() throws Exception{
        // given
        UserToAdminResponseDto userDto = UserToAdminResponseDto.builder()
                .firstName("armen")
                .lastName("mirzoyan")
                .email("armen@example.com")
                .blocked(false)
                .build();

        Page<UserToAdminResponseDto> page = new PageImpl<>(List.of(userDto));

        when(adminService.findUsers(any(UserAdminSearchRequestDto.class), any(Pageable.class)))
                .thenReturn(page);


        // when / then
        mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("armen"))
                .andExpect(jsonPath("$.content[0].email").value("armen@example.com"))
                .andExpect(jsonPath("$.content[0].lastName").value("mirzoyan"))
                .andExpect(jsonPath("$.content[0].blocked").value(false));
    }

    @Test
    void findUsersByRef() throws Exception{
        // given
        UserToAdminDetailedResponseDto userDto = UserToAdminDetailedResponseDto.builder()
                .ref(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .firstName("Armen")
                .lastName("Mirzoyan")
                .email("armen@example.com")
                .blocked(false)
                .createdDate(LocalDateTime.of(2024, 9, 29, 12, 0))
                .deletedDateTime(LocalDateTime.of(2024, 9, 29, 13, 0))
                .build();

        when(adminService.findUserByRef(any()))
                .thenReturn(userDto);


        // when / then
        mockMvc.perform(get("/api/v1/admin/users/{ref}", "123e4567-e89b-12d3-a456-426614174000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Armen"))
                .andExpect(jsonPath("$.lastName").value("Mirzoyan"))
                .andExpect(jsonPath("$.email").value("armen@example.com"))
                .andExpect(jsonPath("$.blocked").value(false))
                .andExpect(jsonPath("$.createdDate").value("2024-09-29T12:00:00Z"))
                .andExpect(jsonPath("$.deletedDateTime").value("2024-09-29T13:00:00Z"));
    }


    @Test
    void blockUser() throws Exception{
        // given
        UUID userRef = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");


        UserUpdateAdminRequestDto requestDto = new UserUpdateAdminRequestDto();
        requestDto.setBlocked(true);

        doNothing().when(adminService).updateUser(eq(userRef), any(UserUpdateAdminRequestDto.class));

        // when / then
        mockMvc.perform(put("/api/v1/admin/users/{ref}", userRef)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect( content().string("User status updated by admin." + userRef));

        // verify that service method was called once
        verify(adminService, times(1)).updateUser(eq(userRef), any(UserUpdateAdminRequestDto.class));
    }

    @Test
    void getAllCards() throws Exception {
        CardResponseDto cardDto = CardResponseDto.builder()
                .ref(UUID.randomUUID())
                .number("1234-5678-9012-3456")
                .build();

        Page<CardResponseDto> page = new PageImpl<>(List.of(cardDto));

        when(adminService.getAllCards(any(CardSearchRequestDto.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/admin/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].number").value("1234-5678-9012-3456"));
    }

    @Test
    void createCard() throws Exception {
        CardCreateRequestDto requestDto = new CardCreateRequestDto();
        requestDto.setUserRef(UUID.randomUUID());
        requestDto.setType("VISA");

        CardResponseDto responseDto = CardResponseDto.builder()
                .ref(UUID.randomUUID())
                .number("1234-5678-9012-3456")
                .build();

        when(adminService.createCardForUser(any(CardCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/admin/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("1234-5678-9012-3456"));
    }

    @Test
    void blockCard() throws Exception {
        UUID ref = UUID.randomUUID();
        doNothing().when(adminService).blockCard(ref);

        mockMvc.perform(put("/api/v1/admin/cards/{ref}/block", ref))
                .andExpect(status().isOk())
                .andExpect(content().string("Card blocked by admin. Card ref : " + ref));
    }

    @Test
    void activateCard() throws Exception {
        UUID ref = UUID.randomUUID();
        doNothing().when(adminService).activateCard(ref);

        mockMvc.perform(put("/api/v1/admin/cards/{ref}/activate", ref))
                .andExpect(status().isOk())
                .andExpect(content().string("Card activated by admin. Card ref : " + ref));
    }

    @Test
    void deleteCard() throws Exception {
        Long ref = 1L;
        doNothing().when(adminService).deleteCard(ref);

        mockMvc.perform(delete("/api/v1/admin/cards/{ref}", ref))
                .andExpect(status().isOk())
                .andExpect(content().string("Card deleted by admin. Card ref : " + ref));
    }
}