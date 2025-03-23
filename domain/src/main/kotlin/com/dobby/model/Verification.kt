package com.dobby.model
import com.dobby.enums.VerificationStatus
import com.dobby.util.TimeProvider
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
        return this.copy(updatedAt = TimeProvider.currentDateTime())
    }

    companion object {
        fun newVerification(
            id : String,
            univEmail: String,
        ) = Verification(
            id = id,
            univEmail = univEmail,
            createdAt = TimeProvider.currentDateTime(),
            updatedAt = TimeProvider.currentDateTime()
        )
    }
}
