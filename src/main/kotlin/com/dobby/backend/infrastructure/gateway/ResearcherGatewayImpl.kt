package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.ResearcherGateway
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.converter.ResearcherConverter
import com.dobby.backend.infrastructure.database.repository.ResearcherRepository
import org.springframework.stereotype.Component

@Component
class ResearcherGatewayImpl(
    private val researcherRepository: ResearcherRepository
) : ResearcherGateway{

    override fun save(researcher: Researcher): Researcher {
        val entity = ResearcherConverter.toEntity(researcher)
        val savedEntity = researcherRepository.save(entity)
        return ResearcherConverter.toModel(savedEntity)
    }
}
