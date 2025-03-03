package com.dobby.persistence.entity

import com.dobby.model.Verification
import com.dobby.enums.VerificationStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "verification")
class VerificationEntity (
    @Id
    @Column(name = "verification_id", columnDefinition = "CHAR(13)")
    val id: String,

    @Column(name = "univ_email", nullable = false, unique = true)
    val univEmail : String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: VerificationStatus = VerificationStatus.HOLD,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime ? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime ? = null,
) {
    @PrePersist
    fun prePersist() {
        createdAt ?: LocalDateTime.now()
        updatedAt ?: LocalDateTime.now()
    }

    fun toDomain() : Verification {
        return Verification(
            id = id,
            univEmail = univEmail,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromDomain(model: Verification) = with(model) {
            VerificationEntity(
                id =  id,
                univEmail = univEmail,
                status = status,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
