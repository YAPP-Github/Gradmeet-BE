package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.enums.member.MemberStatus
import com.dobby.backend.domain.enums.member.ProviderType
import com.dobby.backend.domain.enums.member.RoleType
import com.dobby.backend.domain.enums.member.WithdrawalReasonType
import com.dobby.backend.domain.exception.MemberNotFoundException
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.gateway.member.MemberWithdrawalGateway
import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Researcher
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class DeleteResearcherUseCaseTest : BehaviorSpec({
    val researcherGateway = mockk<ResearcherGateway>()
    val memberWithdrawalGateway = mockk<MemberWithdrawalGateway>(relaxed = true)
    val verificationGateway = mockk<VerificationGateway>(relaxed = true)
    val deleteResearcherUseCase = DeleteResearcherUseCase(
        researcherGateway,
        memberWithdrawalGateway,
        verificationGateway
    )

    given("유효한 연구자가 존재하는 경우") {
        val memberId = "1"
        val reasonType = WithdrawalReasonType.SECURITY_CONCERN
        val reason = null
        val member = Member(
            id = memberId,
            name = "지수r",
            oauthEmail = "researcher@example.com",
            contactEmail = "contact@example.com",
            provider = ProviderType.GOOGLE,
            status = MemberStatus.ACTIVE,
            role = RoleType.RESEARCHER,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )
        val researcher = Researcher(
            id = "1",
            member = member,
            univEmail = "university@example.com",
            emailVerified = true,
            univName = "Some University",
            major = "Computer Science",
            labInfo = "Lab Info"
        )

        every { researcherGateway.findByMemberId(memberId) } returns researcher
        every { researcherGateway.save(any()) } returns researcher.withdraw()

        `when`("DeleteResearcherUseCase가 실행되면") {
            val input = DeleteResearcherUseCase.Input(memberId, reasonType, reason)
            val output = deleteResearcherUseCase.execute(input)

            then("isSuccess가 true여야 한다") {
                output.isSuccess shouldBe true
            }

            then("verificationGateway.deleteByUnivEmail이 연구자의 univEmail로 호출되어야 한다") {
                verify { verificationGateway.deleteByUnivEmail("university@example.com") }
            }

            then("memberWithdrawalGateway.save가 올바른 탈퇴 기록으로 호출되어야 한다") {
                verify {
                    memberWithdrawalGateway.save(match {
                        it.memberId == memberId &&
                                it.reasonType == reasonType &&
                                it.otherReason == reason
                    })
                }
            }
        }
    }

    given("연구자가 존재하지 않는 경우") {
        val memberId = "non-existent"
        every { researcherGateway.findByMemberId(memberId) } returns null

        `when`("DeleteResearcherUseCase가 실행되면") {
            then("MemberNotFoundException 예외가 발생해야 한다") {
                shouldThrow<MemberNotFoundException> {
                    deleteResearcherUseCase.execute(
                        DeleteResearcherUseCase.Input(memberId, WithdrawalReasonType.RESEARCH_STOPPED, null)
                    )
                }
            }
        }
    }
})
