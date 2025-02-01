package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.member.ParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRepository: JpaRepository<ParticipantEntity, String> {
    fun findByMemberId(memberId: String): ParticipantEntity?
}
