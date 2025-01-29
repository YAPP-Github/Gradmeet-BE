package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.CodeExpiredException
import com.dobby.backend.domain.exception.CodeNotCorrectException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus

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
        val info = verificationGateway.findByUnivEmail(input.univEmail)
            ?: throw VerifyInfoNotFoundException()

        if(!info.verifyCode(input.inputCode))
            throw CodeNotCorrectException()

        if(info.isExpired())
            throw CodeExpiredException()

        verificationGateway.updateStatus(info.univEmail, VerificationStatus.VERIFIED)

        return Output(
            isSuccess = true,
            message = "학교 메일 인증이 완료되었습니다."
        )
    }
}
