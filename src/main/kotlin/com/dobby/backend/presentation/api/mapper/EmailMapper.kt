package com.dobby.backend.presentation.api.mapper

import com.dobby.backend.application.usecase.signup.email.EmailCodeSendUseCase
import com.dobby.backend.application.usecase.signup.email.EmailVerificationUseCase
import com.dobby.backend.presentation.api.dto.request.signup.EmailSendRequest
import com.dobby.backend.presentation.api.dto.request.signup.EmailVerificationRequest
import com.dobby.backend.presentation.api.dto.response.signup.EmailSendResponse
import com.dobby.backend.presentation.api.dto.response.signup.EmailVerificationResponse

object EmailMapper {

    fun toEmailCodeSendUseCaseInput(request: EmailSendRequest): EmailCodeSendUseCase.Input {
        return EmailCodeSendUseCase.Input(
            univEmail = request.univEmail
        )
    }

    fun toEmailSendResponse(output: EmailCodeSendUseCase.Output): EmailSendResponse {
        return EmailSendResponse(
            isSuccess = output.isSuccess,
            message = output.message
        )
    }

    fun toEmailVerificationUseCaseInput(request: EmailVerificationRequest): EmailVerificationUseCase.Input {
        return EmailVerificationUseCase.Input(
            univEmail = request.univEmail,
            inputCode = request.inputCode
        )
    }

    fun toEmailVerificationResponse(output: EmailVerificationUseCase.Output): EmailVerificationResponse {
        return EmailVerificationResponse(
            isSuccess = output.isSuccess,
            message = output.message
        )
    }
}
