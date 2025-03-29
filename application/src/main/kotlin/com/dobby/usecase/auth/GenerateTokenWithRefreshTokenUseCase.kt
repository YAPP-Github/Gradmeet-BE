package com.dobby.usecase.auth

import com.dobby.gateway.auth.TokenGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.model.member.Member
import com.dobby.usecase.UseCase

class GenerateTokenWithRefreshTokenUseCase(
    private val tokenGateway: TokenGateway,
    private val memberGateway: MemberGateway
) : UseCase<GenerateTokenWithRefreshTokenUseCase.Input, GenerateTokenWithRefreshTokenUseCase.Output> {
    data class Input(
        val refreshToken: String
    )

    data class Output(
        val accessToken: String,
        val refreshToken: String,
        val member: Member
    )

    override fun execute(input: Input): Output {
        val memberId = tokenGateway.extractMemberIdFromRefreshToken(input.refreshToken)
        val member = memberGateway.getById(memberId)
        return Output(
            accessToken = tokenGateway.generateAccessToken(member),
            refreshToken = tokenGateway.generateRefreshToken(member),
            member = member
        )
    }
}
