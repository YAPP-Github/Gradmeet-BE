package com.dobby.backend.infrastructure.gateway.member

import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.database.entity.member.ResearcherEntity
import com.dobby.backend.infrastructure.database.repository.ResearcherRepository
import org.springframework.stereotype.Component

@Component
class ResearcherGatewayImpl(
    private val researcherRepository: ResearcherRepository
) : ResearcherGateway {

    override fun findByMemberId(memberId: String) : Researcher? {
        return researcherRepository
            .findByMemberId(memberId)
            ?.let(ResearcherEntity::toDomain)
    }

    override fun findByMemberIdAndMemberDeletedAtIsNull(memberId: String): Researcher? {
        return researcherRepository
            .findByMemberIdAndMemberDeletedAtIsNull(memberId)
            ?.let(ResearcherEntity::toDomain)
    }

    override fun save(researcher: Researcher): Researcher {
        val savedEntity = researcherRepository
            .save(ResearcherEntity.fromDomain(researcher))
        return savedEntity.toDomain()
    }
}
