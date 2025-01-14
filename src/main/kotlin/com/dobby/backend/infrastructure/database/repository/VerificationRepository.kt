package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationRepository : JpaRepository<VerificationEntity, Long> {
    fun findByUnivEmailAndStatus(univEmail: String, verified: VerificationStatus): VerificationEntity?
    fun findByUnivEmail(univEmail: String): VerificationEntity?
}
