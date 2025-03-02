package com.dobby.backend.application.usecase.auth

import com.dobby.domain.exception.MemberNotFoundException
import com.dobby.domain.gateway.member.MemberGateway
import io.kotest.core.spec.style.BehaviorSpec
import com.dobby.domain.gateway.auth.TokenGateway
import com.dobby.domain.model.member.Member
import com.dobby.domain.enums.member.MemberStatus
import com.dobby.domain.enums.member.ProviderType
import com.dobby.domain.enums.member.RoleType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class GenerateTestTokenUseCaseTest: BehaviorSpec({
    val tokenGateway = mockk<TokenGateway>()
    val memberGateway = mockk<MemberGateway>()
    val generateTestTokenUseCase = GenerateTestTokenUseCase(tokenGateway, memberGateway)

    given("memberId가 주어졌을 때") {
        val member = Member(id = "1", oauthEmail = "dlawotn3@naver.com", contactEmail = "dlawotn3@naver.com",
            provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, name = "dobby",
            status = MemberStatus.ACTIVE, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now(), deletedAt = null)
        val accessToken = "testAccessToken"
        val refreshToken = "testRefreshToken"

        every { tokenGateway.generateAccessToken(member) } returns accessToken
        every { tokenGateway.generateRefreshToken(member) } returns refreshToken
        every { memberGateway.findByIdAndDeletedAtIsNull("1") } returns member

        `when`("execute가 호출되면") {
            val input = GenerateTestTokenUseCase.Input(member.id)
            val result = generateTestTokenUseCase.execute(input)

            then("생성된 accessToken과 refreshToken이 반환되어야 한다") {
                result.accessToken shouldBe accessToken
                result.refreshToken shouldBe refreshToken
            }
        }

        every { memberGateway.findByIdAndDeletedAtIsNull("2") } returns null

        `when`("존재하지 않는 회원 ID가 주어졌을 때") {
            val input = GenerateTestTokenUseCase.Input("2")

            then("MemberNotFoundException 예외가 던져져야 한다") {
                shouldThrow<MemberNotFoundException> {
                    generateTestTokenUseCase.execute(input)
                }
            }
        }
    }
})
