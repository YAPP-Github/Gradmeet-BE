package com.dobby.usecase.member

import com.dobby.usecase.UseCase
import com.dobby.exception.EmailNotValidateException
import com.dobby.gateway.email.VerificationGateway
import com.dobby.enums.VerificationStatus

class VerifyResearcherEmailUseCase(
    private val verificationGateway: VerificationGateway
) : UseCase<String, Unit> {

    override fun execute(input: String) {
        verificationGateway.findByUnivEmailAndStatus(input, VerificationStatus.VERIFIED)
            ?: throw EmailNotValidateException
        return
    }

}
