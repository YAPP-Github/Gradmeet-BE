package com.dobby.backend.domain.gateway.member

import com.dobby.backend.domain.model.member.Researcher

interface ResearcherGateway {
    fun findByMemberId(memberId: String): Researcher?
    fun findByMemberIdAndMemberDeletedAtIsNull(memberId: String): Researcher?
    fun existsByUnivEmail(univEmail: String): Boolean
    fun save(researcher: Researcher): Researcher
}
