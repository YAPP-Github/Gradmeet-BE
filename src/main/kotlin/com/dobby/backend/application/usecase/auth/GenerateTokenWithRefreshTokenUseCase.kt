package com.dobby.backend.application.usecase.auth

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.domain.model.member.Member

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
        val memberId = tokenGateway.extractMemberIdFromRefreshToken(input.refreshToken).toLong()
        val member = memberGateway.getById(memberId)
        return Output(
            accessToken = tokenGateway.generateAccessToken(member),
            refreshToken = tokenGateway.generateRefreshToken(member),
            member = member
        )
    }
}
