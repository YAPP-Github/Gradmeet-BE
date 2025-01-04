package com.dobby.backend.application.usecase

import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.domain.gateway.TokenGateway

class GenerateTestToken(
    private val tokenGateway: TokenGateway,
    private val memberGateway: MemberGateway,
) : UseCase<GenerateTestToken.Input, GenerateTestToken.Output> {
    data class Input(
        val memberId: Long
    )

    data class Output(
        val accessToken: String,
        val refreshToken: String,
    )

    override fun execute(input: Input): Output {
        val memberId = input.memberId
        val member = memberGateway.getById(memberId)
        return Output(
            accessToken = tokenGateway.generateAccessToken(member),
            refreshToken = tokenGateway.generateRefreshToken(member)
        )
    }
}
