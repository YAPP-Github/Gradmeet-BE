package com.dobby.backend.domain.model
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import java.time.LocalDateTime

data class Verification(
    val id: String,
    val univEmail: String,
    var status: VerificationStatus = VerificationStatus.HOLD,
    val createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {
    fun complete(): Verification {
        return this.copy(status = VerificationStatus.VERIFIED)
    }

    companion object {
        fun newVerification(
            id : String,
            univEmail: String,
        ) = Verification(
            id = id,
            univEmail = univEmail,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}
