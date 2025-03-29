package com.dobby.persistence.repository

import com.dobby.enums.VerificationStatus
import com.dobby.persistence.entity.VerificationEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface VerificationRepository : JpaRepository<VerificationEntity, String> {
    fun findByUnivEmailAndStatus(univEmail: String, verified: VerificationStatus): VerificationEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByUnivEmail(univEmail: String): VerificationEntity?
    fun deleteByUnivEmail(univEmail: String)
}
