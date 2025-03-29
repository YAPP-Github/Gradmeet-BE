package com.dobby.usecase.member

import com.dobby.enums.VerificationStatus
import com.dobby.exception.EmailNotValidateException
import com.dobby.gateway.email.VerificationGateway
import com.dobby.usecase.UseCase

class VerifyResearcherEmailUseCase(
    private val verificationGateway: VerificationGateway
) : UseCase<String, Unit> {

    override fun execute(input: String) {
        verificationGateway.findByUnivEmailAndStatus(input, VerificationStatus.VERIFIED)
            ?: throw EmailNotValidateException
        return
    }
}
