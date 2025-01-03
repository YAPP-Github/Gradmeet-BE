package com.dobby.backend.domain.usecase

import com.dobby.backend.application.usecase.GenerateTokenWithRefreshToken
import com.dobby.backend.domain.gateway.TokenGateway
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GenerateTokenWithRefreshTokenTest : BehaviorSpec({
    val tokenGateway = mockk<TokenGateway>()
    val generateTokenWithRefreshToken = GenerateTokenWithRefreshToken(tokenGateway)

    given("유효한 리프레시 토큰이 주어졌을 때") {
        val validRefreshToken = "validRefreshToken"
        val memberId = 123L
        val accessToken = "newAccessToken"
        val newRefreshToken = "newRefreshToken"

        every { tokenGateway.extractMemberIdFromRefreshToken(validRefreshToken) } returns memberId.toString()
        every { tokenGateway.generateAccessToken(memberId) } returns accessToken
        every { tokenGateway.generateRefreshToken(memberId) } returns newRefreshToken

        `when`("execute가 호출되면") {
            val input = GenerateTokenWithRefreshToken.Input(refreshToken = validRefreshToken)
            val result = generateTokenWithRefreshToken.execute(input)

            then("accessToken과 refreshToken이 생성되고, memberId가 포함된다") {
                result.accessToken shouldBe accessToken
                result.refreshToken shouldBe newRefreshToken
                result.memberId shouldBe memberId
            }
        }
    }

    given("유효하지 않은 리프레시 토큰이 주어졌을 때") {
        val invalidRefreshToken = "invalidRefreshToken"

        every { tokenGateway.extractMemberIdFromRefreshToken(invalidRefreshToken) } throws IllegalArgumentException("Invalid refresh token")

        `when`("execute가 호출되면") {
            val input = GenerateTokenWithRefreshToken.Input(refreshToken = invalidRefreshToken)

            then("예외가 발생한다") {
                val exception = runCatching { generateTokenWithRefreshToken.execute(input) }.exceptionOrNull()
                exception shouldBe IllegalArgumentException("Invalid refresh token")
            }
        }
    }
})
