package com.dobby.gateway.member

import com.dobby.model.member.Researcher

interface ResearcherGateway {
    fun findByMemberId(memberId: String): Researcher?
    fun findByMemberIdAndMemberDeletedAtIsNull(memberId: String): Researcher?
    fun existsByUnivEmail(univEmail: String): Boolean
    fun save(researcher: Researcher): Researcher
}
