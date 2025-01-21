package com.dobby.backend.infrastructure.gateway.member

import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.infrastructure.database.entity.enums.MemberStatus
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.converter.MemberConverter
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class MemberGatewayImpl(
    private val memberRepository: MemberRepository,
) : MemberGateway {
    override fun getById(memberId: Long): Member {
        return memberRepository
            .getReferenceById(memberId)
            .let(MemberEntity::toDomain)
    }

    override fun findById(memberId: Long): Member? {
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
        val entity = MemberConverter.toEntity(savedMember)
        val savedEntity = memberRepository.save(entity)
        return MemberConverter.toModel(savedEntity)
    }
}
