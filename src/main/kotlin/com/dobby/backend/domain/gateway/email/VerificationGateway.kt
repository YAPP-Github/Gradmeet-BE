package com.dobby.backend.domain.gateway.email

import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus

interface VerificationGateway {
    fun findByUnivEmailAndStatus(univEmail: String, status: VerificationStatus): Verification?
    fun findByUnivEmail(univEmail: String): Verification?
    fun save(verification: Verification): Verification
    fun updateCode(univEmail: String, code: String)
    fun updateStatus(univEmail: String, status: VerificationStatus)
}
