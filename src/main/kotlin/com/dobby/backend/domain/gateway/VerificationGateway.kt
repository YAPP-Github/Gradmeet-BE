package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus

interface VerificationGateway {
    fun findByUnivEmailAndStatus(univEmail: String, status: VerificationStatus): Verification?
    fun findByUnivEmail(univEmail: String): Verification?
    fun save(verification: Verification): Verification
}
