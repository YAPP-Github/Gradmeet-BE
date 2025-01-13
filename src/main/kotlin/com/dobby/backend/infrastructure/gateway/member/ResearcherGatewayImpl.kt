package com.dobby.backend.infrastructure.gateway.member

import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.converter.ResearcherConverter
import com.dobby.backend.infrastructure.database.repository.ResearcherRepository
import org.springframework.stereotype.Component

@Component
class ResearcherGatewayImpl(
    private val researcherRepository: ResearcherRepository
) : ResearcherGateway {

    override fun findByMemberId(memberId: Long) : Researcher? {
        val researcherEntity = researcherRepository
            .findByMemberId(memberId)
        return researcherEntity?.let { ResearcherConverter.toModel(it) }
    }
    override fun save(researcher: Researcher): Researcher {
        val entity = ResearcherConverter.toEntity(researcher)
        val savedEntity = researcherRepository.save(entity)
        return ResearcherConverter.toModel(savedEntity)
    }
}
