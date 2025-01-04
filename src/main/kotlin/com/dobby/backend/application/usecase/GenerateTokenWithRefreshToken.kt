package com.dobby.backend.application.usecase

import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.domain.gateway.TokenGateway

class GenerateTokenWithRefreshToken(
    private val tokenGateway: TokenGateway,
    private val memberGateway: MemberGateway,
) : UseCase<GenerateTokenWithRefreshToken.Input, GenerateTokenWithRefreshToken.Output> {
    data class Input(
        val refreshToken: String,
    )

    data class Output(
        val accessToken: String,
        val refreshToken: String,
        val memberId: Long
    )

    override fun execute(input: Input): Output {
        val memberId = tokenGateway.extractMemberIdFromRefreshToken(input.refreshToken).toLong()
        val member = memberGateway.getById(memberId)
        return Output(
            accessToken = tokenGateway.generateAccessToken(member),
            refreshToken = tokenGateway.generateRefreshToken(member),
            memberId = memberId
        )
    }
}
