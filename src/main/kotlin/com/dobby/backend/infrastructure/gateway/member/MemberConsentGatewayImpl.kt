package com.dobby.backend.infrastructure.gateway.member

import com.dobby.backend.domain.gateway.member.MemberConsentGateway
import com.dobby.backend.domain.model.member.MemberConsent
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.database.entity.member.MemberConsentEntity
import com.dobby.backend.infrastructure.database.entity.member.ResearcherEntity
import com.dobby.backend.infrastructure.database.repository.MemberConsentRepository
import org.springframework.stereotype.Component

@Component
class MemberConsentGatewayImpl(
    private val memberConsentRepository: MemberConsentRepository
) : MemberConsentGateway {
    override fun save(savedConsent: MemberConsent): MemberConsent {
        val savedEntity = memberConsentRepository
            .save(MemberConsentEntity.fromDomain(savedConsent))
        return savedEntity.toDomain()
    }

    override fun findByMemberId(memberId: String): MemberConsent? {
        return memberConsentRepository
                .findByMemberId(memberId)?.toDomain()
    }
}
