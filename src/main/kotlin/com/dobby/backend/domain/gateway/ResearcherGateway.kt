package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Researcher

interface ResearcherGateway {
    fun findByMemberId(memberId: Long): Researcher?

    fun save(researcher: Researcher): Researcher
}
