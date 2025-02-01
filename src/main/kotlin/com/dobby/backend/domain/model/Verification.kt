package com.dobby.backend.domain.model
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import com.dobby.backend.util.generateTSID
import java.time.LocalDateTime

data class Verification(
    val id: Long,
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

    fun complete(): Verification {
        return this.copy(status = VerificationStatus.VERIFIED)
    }

    companion object {
        fun newVerification(
           univEmail: String,
           verificationCode: String,
        ) = Verification(
            id = generateTSID(),
            univEmail = univEmail,
            verificationCode = verificationCode,
            expiresAt = LocalDateTime.now().plusMinutes(10),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}
