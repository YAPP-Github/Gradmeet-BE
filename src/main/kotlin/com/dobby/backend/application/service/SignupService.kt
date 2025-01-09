package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.signupUseCase.ParticipantSignupUseCase
import com.dobby.backend.application.usecase.signupUseCase.CreateResearcherUseCase
import com.dobby.backend.application.usecase.signupUseCase.VerifyResearcherEmailUseCase
import com.dobby.backend.domain.exception.EmailNotValidateException
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SignupService(
    private val participantSignupUseCase: ParticipantSignupUseCase,
    private val createResearcherUseCase: CreateResearcherUseCase,
    private val verifyResearcherEmailUseCase: VerifyResearcherEmailUseCase
) {
    @Transactional
    fun participantSignup(input: ParticipantSignupRequest): SignupResponse {
           return participantSignupUseCase.execute(input)
    }

    @Transactional
    fun researcherSignup(input: ResearcherSignupRequest) : SignupResponse{
        if(!input.emailVerified) {
            throw EmailNotValidateException()
        }
        verifyResearcherEmailUseCase.execute(input.univEmail)

        return createResearcherUseCase.execute(input)
    }

}
