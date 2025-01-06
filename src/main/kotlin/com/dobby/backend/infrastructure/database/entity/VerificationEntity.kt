package com.dobby.backend.infrastructure.database.entity

import AuditingEntity
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "VERIFICATION")
class VerificationEntity (
    @Id
    @Column(name = "verification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "univ_mail", nullable = false, unique = true)
    val univMail : String,

    @Column(name = "verification_code", length = 6, nullable = false, unique = true)
    val verificationCode: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: VerificationStatus= VerificationStatus.HOLD,

    @Column(name = "expires_at", nullable = false)
    var expiresAt : LocalDateTime ? = null
): AuditingEntity() {
    @PrePersist
    fun prePersist(){  if(expiresAt == null) expiresAt = LocalDateTime.now().plusMinutes(10)}
}
