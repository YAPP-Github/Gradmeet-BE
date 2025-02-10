package com.dobby.backend.infrastructure.gateway.member

import com.dobby.backend.domain.gateway.member.MemberWithdrawalGateway
import com.dobby.backend.domain.model.member.MemberWithdrawal
import com.dobby.backend.infrastructure.database.entity.member.MemberWithdrawalEntity
import com.dobby.backend.infrastructure.database.repository.MemberWithdrawalRepository
import org.springframework.stereotype.Component

@Component
class MemberWithdrawalGatewayImpl(
    private val memberWithdrawalRepository: MemberWithdrawalRepository
) : MemberWithdrawalGateway {
    override fun save(memberWithdrawal: MemberWithdrawal): MemberWithdrawal {
        val savedEntity = memberWithdrawalRepository
            .save(MemberWithdrawalEntity.fromDomain(memberWithdrawal))
        return savedEntity.toDomain()
    }
}
