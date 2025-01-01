package com.dobby.backend.domain.usecase

import io.kotest.core.spec.style.BehaviorSpec
import com.dobby.backend.domain.gateway.TokenGateway
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GenerateTestTokenTest: BehaviorSpec({
    val tokenGateway = mockk<TokenGateway>()
    val generateTestToken = GenerateTestToken(tokenGateway)

    given("memberId가 주어졌을 때") {
        val memberId = 123L
        val accessToken = "testAccessToken"
        val refreshToken = "testRefreshToken"

        every { tokenGateway.generateAccessTokenForTestMember(memberId) } returns accessToken
        every { tokenGateway.generateRefreshTokenForTestMember(memberId) } returns refreshToken

        `when`("execute가 호출되면") {
            val input = GenerateTestToken.Input(memberId)
            val result = generateTestToken.execute(input)

            then("생성된 accessToken과 refreshToken이 반환되어야 한다") {
                result.accessToken shouldBe accessToken
                result.refreshToken shouldBe refreshToken
            }
        }
    }
})
