package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface VerificationRepository : JpaRepository<VerificationEntity, Long> {
    fun findByUnivMailAndStatus(univEmail: String, verified: VerificationStatus): VerificationEntity?
    fun findByUnivMail(univEmail: String): VerificationEntity?
}
