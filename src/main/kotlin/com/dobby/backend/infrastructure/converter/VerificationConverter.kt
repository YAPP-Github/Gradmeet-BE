package com.dobby.backend.infrastructure.converter

import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.VerificationEntity

object VerificationConverter {
    fun toModel(entity: VerificationEntity): Verification {
        return Verification(
            id = entity.id,
            univEmail = entity.univEmail,
            verificationCode = entity.verificationCode,
            status = entity.status,
            expiresAt = entity.expiresAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toEntity(model: Verification): VerificationEntity {
        return VerificationEntity(
            id = 0L,
            univEmail = model.univEmail,
            verificationCode = model.verificationCode,
            status = model.status,
            expiresAt = model.expiresAt,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )
    }
}
