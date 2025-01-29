package com.dobby.backend.infrastructure.gateway.email

import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import com.dobby.backend.infrastructure.database.repository.VerificationRepository
import com.dobby.backend.infrastructure.converter.VerificationConverter
import org.springframework.stereotype.Component

@Component
class VerificationGatewayImpl(
    private val verificationRepository: VerificationRepository
) : VerificationGateway {

    override fun findByUnivEmailAndStatus(univEmail: String, status: VerificationStatus): Verification? {
        return verificationRepository
            .findByUnivEmailAndStatus(univEmail, status)
            ?.let(VerificationConverter::toModel)
    }

    override fun findByUnivEmail(univEmail: String): Verification? {
        return verificationRepository
            .findByUnivEmail(univEmail)
            ?.let(VerificationConverter::toModel)
    }

    override fun save(verification: Verification): Verification {
        val entity = VerificationConverter.toEntity(verification)
        return verificationRepository
            .save(entity)
            .let(VerificationConverter::toModel)
    }

    override fun updateCode(univEmail: String, code: String){
        return verificationRepository.updateCode(univEmail, code)
    }

    override fun updateStatus(univEmail: String, status: VerificationStatus) {
        return verificationRepository.updateStatus(univEmail, status)
    }
}
