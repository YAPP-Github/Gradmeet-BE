package com.dobby.persistence.repository

import com.dobby.persistence.entity.member.ParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRepository : JpaRepository<ParticipantEntity, String> {
    fun findByMemberId(memberId: String): ParticipantEntity?
}
