package com.dobby.backend.application.mapper

import com.dobby.backend.application.usecase.signup.email.EmailCodeSendUseCase
import com.dobby.backend.application.usecase.signup.email.EmailVerificationUseCase
import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.presentation.api.dto.request.signup.EmailSendRequest

object VerificationMapper {
    fun toEntity(req: EmailSendRequest, code : String): VerificationEntity {
        return VerificationEntity(
            id= 0,
            univEmail = req.univEmail,
            verificationCode = code,
            status = VerificationStatus.HOLD,
            expiresAt = null
        )
    }

    fun toSendResDto(isSuccess: Boolean, message: String) : EmailCodeSendUseCase.Output{
        return EmailCodeSendUseCase.Output(
            isSuccess = isSuccess,
            message = message
        )
    }

    fun toVerifyResDto(isSuccess: Boolean, message: String) : EmailVerificationUseCase.Output {
        return EmailVerificationUseCase.Output(
            isSuccess = isSuccess,
            message = message
        )
    }
}
