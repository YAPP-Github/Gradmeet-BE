package com.dobby.backend.application.mapper

import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.presentation.api.dto.request.signup.EmailSendRequest
import com.dobby.backend.presentation.api.dto.response.signup.EmailSendResponse
import com.dobby.backend.presentation.api.dto.response.signup.EmailVerificationResponse

object VerificationMapper {
    fun toEntity(req: EmailSendRequest, code : String): VerificationEntity {
        return VerificationEntity(
            id= 0,
            univMail = req.univEmail,
            verificationCode = code,
            status = VerificationStatus.HOLD,
            expiresAt = null
        )
    }

    fun toSendResDto() : EmailSendResponse{
        return EmailSendResponse(
            isSuccess = true,
            message = "해당 학교 이메일로 성공적으로 코드를 전송했습니다. 10분 이내로 인증을 완료해주세요."
        )
    }

    fun toVerifyResDto() : EmailVerificationResponse {
        return EmailVerificationResponse(
            isSuccess = true,
            message = "학교 메일 인증이 완료되었습니다."
        )
    }
}
