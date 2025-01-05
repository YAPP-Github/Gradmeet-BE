package com.dobby.backend.application.usecase

import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.domain.model.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class GenerateTokenWithRefreshTokenTest : BehaviorSpec({
    val tokenGateway = mockk<TokenGateway>()
    val memberGateway = mockk<MemberGateway>()
    val generateTokenWithRefreshToken = GenerateTokenWithRefreshToken(tokenGateway, memberGateway)

    given("유효한 리프레시 토큰이 주어졌을 때") {
        val validRefreshToken = "validRefreshToken"
        val member = Member(memberId = 1, oauthEmail = "dlawotn3@naver.com", contactEmail = "dlawotn3@naver.com",
            provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, name = "dobby",
            birthDate = LocalDate.of(2000, 7, 8), status = MemberStatus.ACTIVE)
        val accessToken = "newAccessToken"
        val newRefreshToken = "newRefreshToken"

        every { tokenGateway.extractMemberIdFromRefreshToken(validRefreshToken) } returns member.memberId.toString()
        every { tokenGateway.generateAccessToken(member) } returns accessToken
        every { tokenGateway.generateRefreshToken(member) } returns newRefreshToken
        every { memberGateway.getById(1) } returns member

        `when`("execute가 호출되면") {
            val input = GenerateTokenWithRefreshToken.Input(refreshToken = validRefreshToken)
            val result = generateTokenWithRefreshToken.execute(input)

            then("accessToken과 refreshToken이 생성되고, memberId가 포함된다") {
                result.accessToken shouldBe accessToken
                result.refreshToken shouldBe newRefreshToken
                result.memberId shouldBe member.memberId
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
