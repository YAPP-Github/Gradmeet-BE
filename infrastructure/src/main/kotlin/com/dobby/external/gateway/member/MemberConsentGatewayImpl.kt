package com.dobby.external.gateway.member

import com.dobby.model.member.MemberConsent
import com.dobby.persistence.entity.member.MemberConsentEntity
import com.dobby.persistence.repository.MemberConsentRepository
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

    override fun updateMatchConsent(memberId: String, matchConsent: Boolean): MemberConsent {
        val entity = memberConsentRepository.findByMemberId(memberId)
            ?: throw IllegalArgumentException("MemberConsent not found for memberId: $memberId")
        entity.matchConsent = matchConsent
        entity.matchConsentedAt = null
        val updatedEntity = memberConsentRepository.save(entity)
        return updatedEntity.toDomain()
    }
}
