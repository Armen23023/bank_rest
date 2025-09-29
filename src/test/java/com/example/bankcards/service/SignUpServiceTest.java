package com.example.bankcards.service;

import com.example.bankcards.dto.signup.request.SignupRequestDto;
import com.example.bankcards.exception.ApiException;
import com.example.bankcards.exception.ErrorCode;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.SignupServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignupServiceImpl signupService;

    @Test
    void signup_shouldSaveUser_whenEmailDoesNotExist() {
        // given
        SignupRequestDto request = new SignupRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("Armen");
        request.setLastName("Mirzoyan");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");

        // when
        signupService.signup(request);

        // then
        verify(userRepository).save(argThat(user ->
                user.getEmail().equals("test@example.com") &&
                        user.getFirstName().equals("Armen") &&
                        user.getLastName().equals("Mirzoyan") &&
                        user.getPassword().equals("encodedPass")
        ));
    }

    @Test
    void signup_shouldThrowException_whenEmailAlreadyExists() {
        // given
        SignupRequestDto request = new SignupRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // when / then
        ApiException ex = assertThrows(ApiException.class, () -> signupService.signup(request));
        assertEquals("Email already exists", ex.getMessage());
        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS, ex.getErrorCode());

        verify(userRepository, never()).save(any());
    }
}
