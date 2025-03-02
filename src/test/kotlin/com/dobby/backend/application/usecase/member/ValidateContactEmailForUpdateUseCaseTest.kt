package com.dobby.backend.application.usecase.member

import com.dobby.exception.ContactEmailDuplicateException
import com.dobby.gateway.member.MemberGateway
import io.kotest.assertions.throwables.shouldThrow
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

            then("사용 가능하다고 판단해야 한다") {
                result.success shouldBe true
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

            then("사용 가능하다고 판단해야 한다") {
                result.success shouldBe true
            }
        }

        `when`("입력된 이메일이 이미 존재하는 경우") {
            every { memberGateway.existsByContactEmail(newEmail) } returns true
            val input = ValidateContactEmailForUpdateUseCase.Input(memberId, newEmail)

            then("ContactEmailDuplicateException 예외가 발생해야 한다") {
                shouldThrow<ContactEmailDuplicateException> {
                    useCase.execute(input)
                }
            }
        }
    }
})
