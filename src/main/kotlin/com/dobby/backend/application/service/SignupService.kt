package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.ParticipantSignupUseCase
import com.dobby.backend.presentation.api.dto.request.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SignupService(
    private val participantSignupUseCase: ParticipantSignupUseCase
) {
    @Transactional
    fun participantSignup(input: ParticipantSignupRequest): SignupResponse {
        return participantSignupUseCase.execute(input)
    }
}