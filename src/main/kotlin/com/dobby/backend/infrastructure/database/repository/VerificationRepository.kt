package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationRepository : JpaRepository<VerificationEntity, Long> {
}
