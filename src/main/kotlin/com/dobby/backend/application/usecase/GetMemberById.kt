package com.dobby.backend.application.usecase

import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.domain.model.Member

class GetMemberById(
    private val memberGateway: MemberGateway,
) : UseCase<GetMemberById.Input, Member> {
    data class Input(
        val memberId: Long,
    )

    override fun execute(input: Input): Member {
        return memberGateway.getById(input.memberId)
    }
}
