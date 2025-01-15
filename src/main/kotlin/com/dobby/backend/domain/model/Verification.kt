package com.dobby.backend.domain.model

import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import java.time.LocalDateTime

data class Verification(
    val verificationId: Long,
    val univEmail: String,
    var verificationCode: String,
    var status: VerificationStatus = VerificationStatus.HOLD,
    var expiresAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {
    fun isExpired(): Boolean {
        return expiresAt?.isBefore(LocalDateTime.now()) == true
    }

    fun verifyCode(inputCode: String): Boolean {
        return verificationCode == inputCode
    }

    fun updateCode(newCode: String) {
        this.verificationCode = newCode
        this.expiresAt = LocalDateTime.now().plusMinutes(10)
    }
}
