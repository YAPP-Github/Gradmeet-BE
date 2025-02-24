package com.dobby.backend.domain.gateway.email

import com.dobby.backend.domain.model.Verification
import com.dobby.backend.domain.enums.VerificationStatus

interface VerificationGateway {
    fun findByUnivEmailAndStatus(univEmail: String, status: VerificationStatus): Verification?
    fun findByUnivEmail(univEmail: String): Verification?
    fun save(verification: Verification): Verification
    fun deleteByUnivEmail(univEmail: String)
}
