package com.dobby.backend.application.usecase.signupUseCase.email

import com.dobby.backend.application.mapper.VerificationMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.CodeExpiredException
import com.dobby.backend.domain.exception.CodeNotCorrectException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.domain.gateway.VerificationGateway
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.infrastructure.database.repository.VerificationRepository
import com.dobby.backend.presentation.api.dto.request.signup.EmailVerificationRequest
import com.dobby.backend.presentation.api.dto.response.signup.EmailVerificationResponse
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime


class EmailVerificationUseCase(
    private val verificationGateway: VerificationGateway
) : UseCase<EmailVerificationUseCase.Input, EmailVerificationUseCase.Output> {

    data class Input (
        val univEmail : String,
        val inputCode: String,
    )

    data class Output (
        val isSuccess: Boolean,
        val message : String
    )

    override fun execute(input: Input): Output {
        val info = verificationGateway.findByUnivEmailAndStatus(
            input.univEmail,
            VerificationStatus.HOLD)
            ?: throw VerifyInfoNotFoundException()

        if(input.inputCode != info.verificationCode)
            throw CodeNotCorrectException()

        if(info.expiresAt?.isBefore(LocalDateTime.now()) == true)
            throw CodeExpiredException()

        info.status = VerificationStatus.VERIFIED
        verificationGateway.save(info)

        return VerificationMapper.toVerifyResDto(
            isSuccess = true,
            message = "학교 메일 인증이 완료되었습니다."
        )
    }
}
