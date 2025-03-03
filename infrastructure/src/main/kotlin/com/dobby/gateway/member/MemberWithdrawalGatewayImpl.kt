package com.dobby.gateway.member

import com.dobby.model.member.MemberWithdrawal
import com.dobby.database.entity.member.MemberWithdrawalEntity
import com.dobby.database.repository.MemberWithdrawalRepository
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
