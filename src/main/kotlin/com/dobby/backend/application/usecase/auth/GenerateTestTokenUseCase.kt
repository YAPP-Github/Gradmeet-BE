package com.dobby.backend.application.usecase.auth

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.MemberNotFoundException
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.auth.TokenGateway
import com.dobby.backend.domain.model.member.Member

class GenerateTestTokenUseCase(
    private val tokenGateway: TokenGateway,
    private val memberGateway: MemberGateway,
) : UseCase<GenerateTestTokenUseCase.Input, GenerateTestTokenUseCase.Output> {
    data class Input(
        val memberId: Long
    )

    data class Output(
        val accessToken: String,
        val refreshToken: String,
        val member: Member
    )

    override fun execute(input: Input): Output {
        val memberId = input.memberId
        val member = memberGateway.findById(memberId)
            ?: throw MemberNotFoundException

        return Output(
            accessToken = tokenGateway.generateAccessToken(member),
            refreshToken = tokenGateway.generateRefreshToken(member),
            member = member
        )
    }
}
