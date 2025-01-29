package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VerificationRepository : JpaRepository<VerificationEntity, Long> {
    fun findByUnivEmailAndStatus(univEmail: String, verified: VerificationStatus): VerificationEntity?
    fun findByUnivEmail(univEmail: String): VerificationEntity?

    @Modifying
    @Query("UPDATE verification v SET v.status = :status WHERE v.univEmail = :univEmail")
    fun updateStatus(univEmail: String, status: VerificationStatus)
}
