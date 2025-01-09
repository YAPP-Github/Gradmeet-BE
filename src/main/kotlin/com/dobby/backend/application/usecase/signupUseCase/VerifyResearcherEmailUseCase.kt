package com.dobby.backend.application.usecase.signupUseCase


import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.EmailNotValidateException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.domain.gateway.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus

class VerifyResearcherEmailUseCase(
    private val verificationGateway: VerificationGateway
) : UseCase<String, Verification> {
    override fun execute(input: String): Verification {
        val verification = verificationGateway.findByUnivEmail(input)
            ?: throw VerifyInfoNotFoundException()

        if (verification.status != VerificationStatus.VERIFIED) {
                throw EmailNotValidateException()
        }
        return verification
    }
}
