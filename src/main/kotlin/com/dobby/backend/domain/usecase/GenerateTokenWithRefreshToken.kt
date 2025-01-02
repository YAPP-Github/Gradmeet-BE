package com.dobby.backend.domain.usecase

import com.dobby.backend.domain.gateway.TokenGateway

class GenerateTokenWithRefreshToken(
    private val tokenGateway: TokenGateway
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
        return Output(
            accessToken = tokenGateway.generateAccessToken(memberId),
            refreshToken = tokenGateway.generateRefreshToken(memberId),
            memberId = memberId
        )
    }
}
