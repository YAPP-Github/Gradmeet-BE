package com.dobby.gateway.email

import com.dobby.model.Verification
import com.dobby.enums.VerificationStatus

interface VerificationGateway {
    fun findByUnivEmailAndStatus(univEmail: String, status: VerificationStatus): Verification?
    fun findByUnivEmail(univEmail: String): Verification?
    fun save(verification: Verification): Verification
    fun deleteByUnivEmail(univEmail: String)
}
