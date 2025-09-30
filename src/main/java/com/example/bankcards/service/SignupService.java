package com.example.bankcards.service;

import com.example.bankcards.dto.signup.request.SignupRequestDto;

public interface SignupService {

    /**
     * Signup user.
     * @param request {@link SignupRequestDto} with details.
     */
    void signup(SignupRequestDto request);

}
