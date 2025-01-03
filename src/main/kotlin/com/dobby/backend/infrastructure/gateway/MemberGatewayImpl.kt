package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.domain.model.Member
import com.dobby.backend.infrastructure.database.entity.MemberEntity
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class MemberGatewayImpl(
    private val jpaMemberRepository: MemberRepository,
) : MemberGateway {
    override fun getById(memberId: Long): Member {
        return jpaMemberRepository
            .getReferenceById(memberId)
            .let(MemberEntity::toDomain)
    }
}
