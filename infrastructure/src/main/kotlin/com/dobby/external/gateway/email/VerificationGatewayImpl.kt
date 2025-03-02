package com.dobby.external.gateway.email

import com.dobby.model.Verification
import com.dobby.enums.VerificationStatus
import com.dobby.gateway.email.VerificationGateway
import com.dobby.persistence.repository.VerificationRepository
import com.dobby.persistence.entity.VerificationEntity
import org.springframework.stereotype.Component

@Component
class VerificationGatewayImpl(
    private val verificationRepository: VerificationRepository
) : VerificationGateway {

    override fun findByUnivEmailAndStatus(univEmail: String, status: VerificationStatus): Verification? {
        val entity =  verificationRepository
            .findByUnivEmailAndStatus(univEmail, status)
        if (entity != null) {
            return entity.toDomain()
        }
        return null
    }

    override fun findByUnivEmail(univEmail: String): Verification? {
        val foundEntity = verificationRepository.findByUnivEmail(univEmail)
        if (foundEntity != null) {
            return foundEntity.toDomain()
        }
        return null
    }

    override fun save(verification: Verification): Verification {
        val savedEntity = verificationRepository
            .save(VerificationEntity.fromDomain(verification))
        return savedEntity.toDomain()
    }

    override fun deleteByUnivEmail(univEmail: String) {
        verificationRepository.deleteByUnivEmail(univEmail)
    }
}
