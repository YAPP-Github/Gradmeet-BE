package com.dobby.backend.application.usecase

import com.dobby.backend.domain.gateway.TokenGateway
<<<<<<< HEAD
<<<<<<< HEAD

class GenerateTestToken(
    private val tokenGateway: TokenGateway
) : UseCase<GenerateTestToken.Input, GenerateTestToken.Output> {
=======
import org.springframework.stereotype.Component
=======
>>>>>>> ce9b9a9 (feat: add @ComponentScan to include UseCase beans in application context)

class GenerateTestToken(
    private val tokenGateway: TokenGateway
) : UseCase<GenerateTestToken.Input,
        GenerateTestToken.Output> {
>>>>>>> 2c43662 (feat: add GenerateTestToken api for test member)
    data class Input(
        val memberId: Long
    )

    data class Output(
        val accessToken: String,
        val refreshToken: String,
    )

    override fun execute(input: Input): Output {
        val memberId = input.memberId
        return Output(
<<<<<<< HEAD
            accessToken = tokenGateway.generateAccessToken(memberId),
            refreshToken = tokenGateway.generateRefreshToken(memberId)
=======
            accessToken = tokenGateway.generateAccessTokenForTestMember(memberId),
            refreshToken = tokenGateway.generateRefreshTokenForTestMember(memberId)
>>>>>>> 2c43662 (feat: add GenerateTestToken api for test member)
        )
    }
}
