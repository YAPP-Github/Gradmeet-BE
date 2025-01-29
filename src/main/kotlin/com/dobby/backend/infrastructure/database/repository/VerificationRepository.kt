package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VerificationRepository : JpaRepository<VerificationEntity, Long> {
    fun findByUnivEmailAndStatus(univEmail: String, verified: VerificationStatus): VerificationEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByUnivEmail(univEmail: String): VerificationEntity?

    @Modifying
    @Query(
        value = """
        UPDATE verification 
        SET verification_code = :code, 
            updated_at = current_timestamp(6), 
            expires_at = DATE_ADD(current_timestamp(6), INTERVAL 10 MINUTE) 
        WHERE univ_email = :univEmail
    """,
        nativeQuery = true
    )
    fun updateCode(univEmail: String, code: String)


    @Modifying
    @Query("UPDATE verification v SET v.status = :status WHERE v.univEmail = :univEmail")
    fun updateStatus(univEmail: String, status: VerificationStatus)
}
