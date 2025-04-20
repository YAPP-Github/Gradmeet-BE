package com.dobby.api.mapper

import com.dobby.api.dto.request.member.EmailSendRequest
import com.dobby.api.dto.request.member.EmailVerificationRequest
import com.dobby.api.dto.response.member.EmailSendResponse
import com.dobby.api.dto.response.member.EmailVerificationResponse
import com.dobby.usecase.member.email.SendEmailCodeUseCase
import com.dobby.usecase.member.email.VerifyEmailUseCase

object EmailMapper {

    fun toEmailCodeSendUseCaseInput(request: EmailSendRequest): SendEmailCodeUseCase.Input {
        return SendEmailCodeUseCase.Input(
            univEmail = request.univEmail
        )
    }

    fun toEmailSendResponse(output: SendEmailCodeUseCase.Output): EmailSendResponse {
        return EmailSendResponse(
            isSuccess = output.isSuccess,
            message = output.message,
            requestCount = output.requestCount,
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
