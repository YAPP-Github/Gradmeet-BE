package com.dobby.persistence.repository

import com.dobby.persistence.entity.member.ResearcherEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ResearcherRepository : JpaRepository<ResearcherEntity, String> {
    fun findByMemberId(memberId: String): ResearcherEntity?
    fun findByMemberIdAndMemberDeletedAtIsNull(memberId: String): ResearcherEntity?
    fun existsByUnivEmail(univEmail: String): Boolean
}
