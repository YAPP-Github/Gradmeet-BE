package com.dobby.backend.application.usecase.SignupUseCase

import com.dobby.backend.application.mapper.VerificationMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.CodeExpiredException
import com.dobby.backend.domain.exception.CodeNotCorrectException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.infrastructure.database.repository.VerificationRepository
import com.dobby.backend.presentation.api.dto.request.signup.EmailVerificationRequest
import com.dobby.backend.presentation.api.dto.response.signup.EmailVerificationResponse
import java.time.LocalDateTime


class EmailVerificationUseCase(
    private val verificationRepository: VerificationRepository
) : UseCase<EmailVerificationRequest, EmailVerificationResponse> {

    override fun execute(input: EmailVerificationRequest): EmailVerificationResponse {
        val info = verificationRepository.findByUnivMailAndStatus(
            input.univEmail,
            VerificationStatus.HOLD)
            ?: throw VerifyInfoNotFoundException()

        if(input.inputCode != info.verificationCode)
            throw CodeNotCorrectException()

        if(info.expiresAt?.isBefore(LocalDateTime.now()) == true)
            throw CodeExpiredException()

        info.status = VerificationStatus.VERIFIED
        verificationRepository.save(info)

        return VerificationMapper.toVerifyResDto()
    }
}
