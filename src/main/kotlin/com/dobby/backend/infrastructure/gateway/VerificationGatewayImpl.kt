package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.infrastructure.database.repository.VerificationRepository
import com.dobby.backend.infrastructure.converter.VerificationConverter
import org.springframework.stereotype.Component

@Component
class VerificationGatewayImpl(
    private val verificationRepository: VerificationRepository
) : VerificationGateway{

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
}
