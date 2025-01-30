package com.dobby.backend.application.usecase.member


import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.EmailNotValidateException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus

class VerifyResearcherEmailUseCase(
    private val verificationGateway: VerificationGateway
) : UseCase<String, Unit> {

    override fun execute(input: String) {
        val verification = verificationGateway
            .findByUnivEmail(input)
            ?: throw VerifyInfoNotFoundException

        if (verification.status != VerificationStatus.VERIFIED) {
            throw EmailNotValidateException
        }
        return
    }

}
