package com.dobby.backend.application.usecase.auth

import com.dobby.backend.application.usecase.UseCase
import com.dobby.domain.gateway.member.MemberGateway
import com.dobby.domain.gateway.auth.TokenGateway
import com.dobby.domain.model.member.Member

class GenerateTokenWithRefreshTokenUseCase(
    private val tokenGateway: TokenGateway,
    private val memberGateway: MemberGateway,
) : UseCase<GenerateTokenWithRefreshTokenUseCase.Input, GenerateTokenWithRefreshTokenUseCase.Output> {
    data class Input(
        val refreshToken: String,
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
