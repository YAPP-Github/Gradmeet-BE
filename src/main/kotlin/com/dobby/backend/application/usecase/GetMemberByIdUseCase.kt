package com.dobby.backend.application.usecase

import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.model.member.Member

class GetMemberByIdUseCase(
    private val memberGateway: MemberGateway,
) : UseCase<GetMemberByIdUseCase.Input, Member> {
    data class Input(
        val memberId: String,
    )

    override fun execute(input: Input): Member {
        return memberGateway.getById(input.memberId)
    }
}
