package com.dobby.gateway.email

import com.dobby.enums.VerificationStatus
import com.dobby.model.Verification

interface VerificationGateway {
    fun findByUnivEmailAndStatus(univEmail: String, status: VerificationStatus): Verification?
    fun findByUnivEmail(univEmail: String): Verification?
    fun save(verification: Verification): Verification
    fun deleteByUnivEmail(univEmail: String)
}
