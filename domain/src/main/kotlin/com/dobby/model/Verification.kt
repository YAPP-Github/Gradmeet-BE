package com.dobby.model
import com.dobby.enums.VerificationStatus
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

    fun update(): Verification {
        return this.copy(updatedAt = LocalDateTime.now())
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
