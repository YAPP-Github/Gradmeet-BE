package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.exception.MemberNotFoundException
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.MemberWithdrawalGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.member.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class DeleteParticipantUseCaseTest : BehaviorSpec({
    val memberGateway = mockk<MemberGateway>()
    val memberWithdrawalGateway = mockk<MemberWithdrawalGateway>(relaxed = true)
    val deleteParticipantUseCase = DeleteParticipantUseCase(memberGateway, memberWithdrawalGateway)

    given("유효한 참여자(Participant)가 존재하는 경우") {
        val memberId = "1"
        val reasonType = WithdrawalReasonType.TOO_MANY_EMAILS
        val reason = null
        val member = Member(
            id = memberId,
            name = "지수",
            oauthEmail = "participant@example.com",
            contactEmail = "contact@example.com",
            provider = ProviderType.GOOGLE,
            status = MemberStatus.ACTIVE,
            role = RoleType.PARTICIPANT,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        every { memberGateway.findByIdAndDeletedAtIsNull(memberId) } returns member
        every { memberGateway.save(any()) } returns member.withdraw()

        `when`("DeleteParticipantUseCase가 실행되면") {
            val input = DeleteParticipantUseCase.Input(memberId, reasonType, reason)
            val output = deleteParticipantUseCase.execute(input)

            then("isSuccess가 true여야 한다") {
                output.isSuccess shouldBe true
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

    given("참여자가 존재하지 않는 경우") {
        val memberId = "non-existent"
        every { memberGateway.findByIdAndDeletedAtIsNull(memberId) } returns null

        `when`("DeleteParticipantUseCase가 실행되면") {
            then("MemberNotFoundException 예외가 발생해야 한다") {
                shouldThrow<MemberNotFoundException> {
                    deleteParticipantUseCase.execute(
                        DeleteParticipantUseCase.Input(memberId, WithdrawalReasonType.TOO_MANY_EMAILS, null)
                    )
                }
            }
        }
    }
})
