package com.dobby.backend.application.usecase

import com.dobby.backend.domain.gateway.MemberGateway
import io.kotest.core.spec.style.BehaviorSpec
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.domain.model.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class GenerateTestTokenTest: BehaviorSpec({
    val tokenGateway = mockk<TokenGateway>()
    val memberGateway = mockk<MemberGateway>()
    val generateTestToken = GenerateTestToken(tokenGateway, memberGateway)

    given("memberId가 주어졌을 때") {
        val member = Member(memberId = 1, oauthEmail = "dlawotn3@naver.com", contactEmail = "dlawotn3@naver.com",
            provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, name = "dobby",
            birthDate = LocalDate.of(2000, 7, 8), status = MemberStatus.ACTIVE)
        val accessToken = "testAccessToken"
        val refreshToken = "testRefreshToken"

        every { tokenGateway.generateAccessToken(member) } returns accessToken
        every { tokenGateway.generateRefreshToken(member) } returns refreshToken
        every { memberGateway.getById(1) } returns member

        `when`("execute가 호출되면") {
            val input = GenerateTestToken.Input(member.memberId)
            val result = generateTestToken.execute(input)

            then("생성된 accessToken과 refreshToken이 반환되어야 한다") {
                result.accessToken shouldBe accessToken
                result.refreshToken shouldBe refreshToken
            }
        }
    }
})
