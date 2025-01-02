package com.dobby.backend.application.service

import com.dobby.backend.domain.exception.MemberNotFoundException
import com.dobby.backend.infrastructure.database.entity.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import com.dobby.backend.presentation.api.dto.request.OauthUserDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class MemberServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val jwtTokenProvider = mockk<com.dobby.backend.infrastructure.token.JwtTokenProvider>()
    val memberService = MemberService(memberRepository, jwtTokenProvider)

    given("존재하는 사용자가 주어졌을 때") {
        `when`("login을 호출하면") {
            val dto = OauthUserDto(
                email = "test@example.com",
                name = "Test User",
                provider = ProviderType.GOOGLE
            )
            val member = Member(
                id = 1L,
                oauthEmail = "test@example.com",
                name = "Test User",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                role = RoleType.PARTICIPANT,
                birthDate = null,
                contactEmail = null
            )
            every { memberRepository.findByOauthEmailAndStatus(dto.email, MemberStatus.ACTIVE) } returns member

            val result = memberService.login(dto)

            then("사용자 객체를 반환한다") {
                result shouldBe member
            }
        }
    }

    given("존재하지 않는 사용자가 주어졌을 때") {
        `when`("login을 호출하면") {
            val dto = OauthUserDto(
                email = "notfound@example.com",
                name = "Test User",
                provider = ProviderType.GOOGLE
            )
            every { memberRepository.findByOauthEmailAndStatus(dto.email, MemberStatus.ACTIVE) } returns null

            then("MemberNotFoundException 예외가 발생한다") {
                shouldThrow<MemberNotFoundException> {
                    memberService.login(dto)
                }
            }
        }
    }
})
