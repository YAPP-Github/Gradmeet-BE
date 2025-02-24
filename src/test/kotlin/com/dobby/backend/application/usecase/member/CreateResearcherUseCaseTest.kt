package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.domain.gateway.auth.TokenGateway
import com.dobby.backend.domain.gateway.member.MemberConsentGateway
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.MemberConsent
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.domain.enums.member.MemberStatus
import com.dobby.backend.domain.enums.member.ProviderType
import com.dobby.backend.domain.enums.member.RoleType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CreateResearcherUseCaseTest : BehaviorSpec({

    val memberGateway: MemberGateway = mockk()
    val researcherGateway: ResearcherGateway = mockk()
    val memberConsentGateway: MemberConsentGateway = mockk()
    val tokenGateway: TokenGateway = mockk()
    val idGenerator: IdGenerator = mockk()

    val createResearcherUseCase = CreateResearcherUseCase(memberGateway, researcherGateway, memberConsentGateway, tokenGateway, idGenerator)

    given("유효한 입력을 받았을 때") {
        val input = CreateResearcherUseCase.Input(
            oauthEmail = "christer10@ewhain.net",
            provider = ProviderType.GOOGLE,
            contactEmail = "christer10@naver.com",
            univEmail = "christer10@ewhain.net",
            univName = "이화여자대학교",
            name = "신수정",
            major = "컴퓨터공학과",
            labInfo = "야뿌 랩실",
            adConsent = true
        )

        val member = Member.newMember(
            id = "1",
            oauthEmail = input.oauthEmail,
            contactEmail = input.contactEmail,
            provider = input.provider,
            role = RoleType.RESEARCHER,
            name = input.name
        )

        val researcher = Researcher.newResearcher(
            id = "1",
            member = member,
            univEmail = input.univEmail,
            univName = input.univName,
            emailVerified = true,
            major = input.major,
            labInfo = input.labInfo
        )

        val memberConsent = MemberConsent.newConsent(
            memberId = member.id,
            adConsent = input.adConsent,
            matchConsent = false,
        )

        val savedMember = member.copy(id = "1", status = MemberStatus.ACTIVE)
        val savedResearcher = researcher.copy(member = savedMember)
        val accessToken = "mock-access-token"
        val refreshToken = "mock-refresh-token"

        every { idGenerator.generateId() } returns "1"
        every { researcherGateway.save(any()) } returns savedResearcher
        every { memberConsentGateway.save(match { it.memberId == member.id }) } returns memberConsent
        every { memberGateway.save(any()) } returns savedMember
        every { tokenGateway.generateAccessToken(savedMember) } returns accessToken
        every { tokenGateway.generateRefreshToken(savedMember) } returns refreshToken

        `when`("useCase의 execute가 호출되고, 새로운 연구자가 생성되면") {
            val output = createResearcherUseCase.execute(input)

            then("저장된 회원 정보는 ACTIVE 상태여야 한다") {
                savedMember.status shouldBe MemberStatus.ACTIVE
            }

            then("액세스 토큰과 리프레시 토큰이 정상적으로 발급되어야 한다") {
                output.accessToken shouldBe accessToken
                output.refreshToken shouldBe refreshToken
            }

            then("회원 정보가 올바르게 반환되어야 한다") {
                output.memberInfo.memberId shouldBe savedMember.id
                output.memberInfo.name shouldBe savedMember.name
                output.memberInfo.oauthEmail shouldBe  savedMember.oauthEmail
                output.memberInfo.provider shouldBe savedMember.provider
                output.memberInfo.role shouldBe savedMember.role
            }
            then("MemberConsent가 저장되어야 한다") {
                verify { memberConsentGateway.save(match { it.memberId == member.id }) }
            }
        }
    }
})
