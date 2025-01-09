package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.model.member.Researcher

interface ResearcherGateway {
    fun save(researcher: Researcher): Researcher
}
