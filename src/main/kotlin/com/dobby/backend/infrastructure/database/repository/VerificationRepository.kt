package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationRepository : JpaRepository<VerificationEntity, Long> {
    fun findByUnivMailAndStatus(univEmail: String, verified: VerificationStatus): VerificationEntity?
}
