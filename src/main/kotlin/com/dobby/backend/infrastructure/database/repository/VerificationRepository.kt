package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.domain.enums.VerificationStatus
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VerificationRepository : JpaRepository<VerificationEntity, String> {
    fun findByUnivEmailAndStatus(univEmail: String, verified: VerificationStatus): VerificationEntity?
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByUnivEmail(univEmail: String): VerificationEntity?
    fun deleteByUnivEmail(univEmail: String)
}
