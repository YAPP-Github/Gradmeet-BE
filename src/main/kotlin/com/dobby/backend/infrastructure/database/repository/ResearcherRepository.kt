package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.member.ResearcherEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ResearcherRepository: JpaRepository<ResearcherEntity, String> {
    fun findByMemberId(memberId: String) : ResearcherEntity?
    fun findByMemberIdAndMemberDeletedAtIsNull(memberId: String): ResearcherEntity?
}
