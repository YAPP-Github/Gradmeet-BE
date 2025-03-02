package com.dobby.backend.application.usecase.member


import com.dobby.backend.application.usecase.UseCase
import com.dobby.domain.exception.EmailNotValidateException
import com.dobby.domain.exception.VerifyInfoNotFoundException
import com.dobby.domain.gateway.email.VerificationGateway
import com.dobby.domain.enums.VerificationStatus

class VerifyResearcherEmailUseCase(
    private val verificationGateway: VerificationGateway
) : UseCase<String, Unit> {

    override fun execute(input: String) {
        verificationGateway.findByUnivEmailAndStatus(input, VerificationStatus.VERIFIED)
            ?: throw EmailNotValidateException
        return
    }

}
