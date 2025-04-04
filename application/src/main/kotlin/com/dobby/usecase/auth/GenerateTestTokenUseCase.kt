package com.dobby.usecase.auth

import com.dobby.exception.MemberNotFoundException
import com.dobby.gateway.auth.TokenGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.model.member.Member
import com.dobby.usecase.UseCase

class GenerateTestTokenUseCase(
    private val tokenGateway: TokenGateway,
    private val memberGateway: MemberGateway
) : UseCase<GenerateTestTokenUseCase.Input, GenerateTestTokenUseCase.Output> {
    data class Input(
        val memberId: String
    )

    data class Output(
        val accessToken: String,
        val refreshToken: String,
        val member: Member
    )

    override fun execute(input: Input): Output {
        val memberId = input.memberId
        val member = memberGateway.findByIdAndDeletedAtIsNull(memberId)
            ?: throw MemberNotFoundException

        return Output(
            accessToken = tokenGateway.generateAccessToken(member),
            refreshToken = tokenGateway.generateRefreshToken(member),
            member = member
        )
    }
}
