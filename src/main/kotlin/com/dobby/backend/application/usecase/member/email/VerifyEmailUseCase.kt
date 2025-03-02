package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.usecase.UseCase
import com.dobby.exception.CodeExpiredException
import com.dobby.exception.CodeNotCorrectException
import com.dobby.exception.EmailAlreadyVerifiedException
import com.dobby.exception.VerifyInfoNotFoundException
import com.dobby.gateway.CacheGateway
import com.dobby.gateway.email.VerificationGateway
import com.dobby.enums.VerificationStatus

class VerifyEmailUseCase(
    private val verificationGateway: VerificationGateway,
    private val cacheGateway: CacheGateway
) : UseCase<VerifyEmailUseCase.Input, VerifyEmailUseCase.Output> {

    data class Input (
        val univEmail : String,
        val inputCode: String,
    )

    data class Output (
        val isSuccess: Boolean,
        val message : String
    )

    override fun execute(input: Input): Output {
        val cachedCode = cacheGateway.get("verification:${input.univEmail}")
            ?: throw CodeExpiredException

        if(cachedCode != input.inputCode){
            throw CodeNotCorrectException
        }

        val info = verificationGateway.findByUnivEmail(input.univEmail)
            ?: throw VerifyInfoNotFoundException

        if(info.status == VerificationStatus.VERIFIED)
            throw EmailAlreadyVerifiedException

        val updatedVerification = info.complete()
        verificationGateway.save(updatedVerification)

        cacheGateway.evict("verification:${input.univEmail}")

        return Output(
            isSuccess = true,
            message = "학교 메일 인증이 완료되었습니다."
        )
    }
}
