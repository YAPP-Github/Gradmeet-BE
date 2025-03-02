package com.dobby.backend.application.usecase.member

import com.dobby.exception.ResearcherNotFoundException
import com.dobby.gateway.member.MemberConsentGateway
import com.dobby.gateway.member.ResearcherGateway
import com.dobby.model.member.Member
import com.dobby.model.member.MemberConsent
import com.dobby.model.member.Researcher
import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.ProviderType
import com.dobby.enums.member.RoleType
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetResearcherInfoUseCaseTest : BehaviorSpec({
    Given("Gateway에 유효한 memberId와 researcherId가 있다면") {
        val researcherGateway = mockk<ResearcherGateway>()
        val memberConsentGateway = mockk<MemberConsentGateway>()
        val useCase = GetResearcherInfoUseCase(researcherGateway, memberConsentGateway)

        val input = GetResearcherInfoUseCase.Input(memberId = "1")
        val mockMember = Member(
            id = "1",
            name = "신수정",
            oauthEmail = "christer10@ewhain.net",
            contactEmail = "christer10@naver.com",
            provider = ProviderType.GOOGLE,
            role = RoleType.RESEARCHER,
            status = MemberStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )
        val mockResearcher = Researcher(
            member = mockMember,
            id = "1",
            univEmail = "christer10@ewhain.net",
            univName = "이화여자대학교",
            major = "컴퓨터공학과",
            labInfo = "야뿌 서버 연구실",
            emailVerified = true,
        )

        val mockMemberConsent = mockk<MemberConsent>(relaxed = true)
        every { researcherGateway.findByMemberId(input.memberId) } returns mockResearcher
        every { memberConsentGateway.findByMemberId(input.memberId) } returns mockMemberConsent

        `when`("GetResearcherInfoUseCase가 실행되면") {
            val result = useCase.execute(input)

            then("올바른 연구자 정보를 반환한다") {
                assertEquals(mockResearcher.member, result.member)
                assertEquals(mockResearcher.univEmail, result.univEmail)
                assertEquals(mockResearcher.univName, result.univName)
                assertEquals(mockResearcher.major, result.major)
                assertEquals(mockResearcher.labInfo, result.labInfo)
            }
        }
    }

    given("gateway에 해당 member 정보가 없으면") {
        val researcherGateway = mockk<ResearcherGateway>()
        val memberConsentGateway = mockk<MemberConsentGateway>()
        val useCase = GetResearcherInfoUseCase(researcherGateway, memberConsentGateway)

        val input = GetResearcherInfoUseCase.Input(memberId = "2")

        every { researcherGateway.findByMemberId(input.memberId) } returns null
        every { memberConsentGateway.findByMemberId(input.memberId) } returns null

        `when`("GetResearcherInfoUseCase가 실행됐을때") {
            then("ResearcherNotFoundException 예외가 반환된다.") {
                assertFailsWith<ResearcherNotFoundException> {
                    useCase.execute(input)
                }
            }
        }
    }
})
