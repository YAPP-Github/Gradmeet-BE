package com.dobby.backend.presentation.api.mapper

import com.dobby.backend.application.usecase.member.email.SendEmailCodeUseCase
import com.dobby.backend.application.usecase.member.email.VerifyEmailUseCase
import com.dobby.backend.presentation.api.dto.request.member.EmailSendRequest
import com.dobby.backend.presentation.api.dto.request.member.EmailVerificationRequest
import com.dobby.backend.presentation.api.dto.response.member.EmailSendResponse
import com.dobby.backend.presentation.api.dto.response.member.EmailVerificationResponse

object EmailMapper {

    fun toEmailCodeSendUseCaseInput(request: EmailSendRequest): SendEmailCodeUseCase.Input {
        return SendEmailCodeUseCase.Input(
            univEmail = request.univEmail
        )
    }

    fun toEmailSendResponse(output: SendEmailCodeUseCase.Output): EmailSendResponse {
        return EmailSendResponse(
            isSuccess = output.isSuccess,
            message = output.message
        )
    }

    fun toEmailVerificationUseCaseInput(request: EmailVerificationRequest): VerifyEmailUseCase.Input {
        return VerifyEmailUseCase.Input(
            univEmail = request.univEmail,
            inputCode = request.inputCode
        )
    }

    fun toEmailVerificationResponse(output: VerifyEmailUseCase.Output): EmailVerificationResponse {
        return EmailVerificationResponse(
            isSuccess = output.isSuccess,
            message = output.message
        )
    }
}
