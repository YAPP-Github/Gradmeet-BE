package com.dobby.backend.application.usecase.signupUseCase

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.EmailNotValidateException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.infrastructure.database.repository.VerificationRepository

class VerifyResearcherEmailUseCase(
    private val verificationRepository: VerificationRepository
) : UseCase<String, VerificationEntity> {
    override fun execute(input: String): VerificationEntity {
        val verificationEntity = verificationRepository.findByUnivEmail(input)
            ?: throw VerifyInfoNotFoundException()

        if (verificationEntity.status != VerificationStatus.VERIFIED) {
                throw EmailNotValidateException()
        }
        return verificationEntity
    }
}
