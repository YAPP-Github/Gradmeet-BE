package com.dobby.backend.infrastructure.gateway.member

import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.infrastructure.database.entity.enums.MemberStatus
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class MemberGatewayImpl(
    private val memberRepository: MemberRepository,
) : MemberGateway {
    override fun getById(memberId: String): Member {
        return memberRepository
            .getReferenceById(memberId)
            .let(MemberEntity::toDomain)
    }

    override fun findById(memberId: String): Member? {
        return memberRepository
            .findById(memberId)
            .map(MemberEntity::toDomain)
            .orElse(null)
    }

    override fun findByOauthEmailAndStatus(email: String, status: MemberStatus): Member? {
        return memberRepository
            .findByOauthEmailAndStatus(email, status)
            ?.let(MemberEntity::toDomain)
    }

    override fun findByOauthEmail(email: String): Member? {
        return memberRepository
            .findByOauthEmail(email)
            ?.let(MemberEntity::toDomain)
    }

    override fun save(savedMember: Member): Member {
        val savedEntity= memberRepository
            .save(MemberEntity.fromDomain(savedMember))
        return savedEntity.toDomain()
    }

    override fun existsByContactEmail(contactEmail: String): Boolean {
        return memberRepository.existsByContactEmail(contactEmail)
    }

    override fun findContactEmailByMemberId(memberId: String): String {
        return memberRepository.findContactEmailById(memberId)
    }

    override fun findByContactEmail(contactEmail: String): Member? {
        return memberRepository
            .findByContactEmail(contactEmail)
            ?.let(MemberEntity::toDomain)
    }
}
