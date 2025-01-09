package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.signupUseCase.email.EmailCodeSendUseCase
import com.dobby.backend.application.usecase.signupUseCase.email.EmailVerificationUseCase
import com.dobby.backend.presentation.api.dto.request.signup.EmailSendRequest
import com.dobby.backend.presentation.api.dto.request.signup.EmailVerificationRequest
import com.dobby.backend.presentation.api.dto.response.signup.EmailSendResponse
import com.dobby.backend.presentation.api.dto.response.signup.EmailVerificationResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val emailCodeSendUseCase: EmailCodeSendUseCase,
    private val emailVerificationUseCase: EmailVerificationUseCase
) {
    @Transactional
    fun sendEmail(req: EmailCodeSendUseCase.Input) : EmailCodeSendUseCase.Output{
        return emailCodeSendUseCase.execute(req)
    }

    @Transactional
    fun verifyCode(req: EmailVerificationUseCase.Input) : EmailVerificationUseCase.Output {
        return emailVerificationUseCase.execute(req)
    }
}
