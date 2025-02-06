package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.gateway.member.MemberGateway
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ValidateContactEmailForUpdateUseCaseTest : BehaviorSpec({

    val memberGateway = mockk<MemberGateway>()
    val useCase = ValidateContactEmailForUpdateUseCase(memberGateway)

    given("회원이 기존 연락처 이메일을 유지하는 경우") {
        val memberId = "member-123"
        val currentEmail = "test@example.com"

        every { memberGateway.findContactEmailByMemberId(memberId) } returns currentEmail

        `when`("입력된 이메일이 기존 이메일과 동일하면") {
            val input = ValidateContactEmailForUpdateUseCase.Input(memberId, currentEmail)
            val result = useCase.execute(input)

            then("중복 이메일이 아니라고 판단해야 한다") {
                result.isDuplicate shouldBe false
            }
        }
    }

    given("회원이 새로운 이메일을 입력하는 경우") {
        val memberId = "member-123"
        val currentEmail = "test@example.com"
        val newEmail = "new@example.com"

        every { memberGateway.findContactEmailByMemberId(memberId) } returns currentEmail

        `when`("입력된 이메일이 중복되지 않은 경우") {
            every { memberGateway.existsByContactEmail(newEmail) } returns false
            val input = ValidateContactEmailForUpdateUseCase.Input(memberId, newEmail)
            val result = useCase.execute(input)

            then("중복 이메일이 아니라고 판단해야 한다") {
                result.isDuplicate shouldBe false
            }
        }

        `when`("입력된 이메일이 이미 존재하는 경우") {
            every { memberGateway.existsByContactEmail(newEmail) } returns true
            val input = ValidateContactEmailForUpdateUseCase.Input(memberId, newEmail)
            val result = useCase.execute(input)

            then("중복 이메일이라고 판단해야 한다") {
                result.isDuplicate shouldBe true
            }
        }
    }
})
