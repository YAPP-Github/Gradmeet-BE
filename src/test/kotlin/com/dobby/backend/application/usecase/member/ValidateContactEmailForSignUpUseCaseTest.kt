package com.dobby.backend.application.usecase.member

import com.dobby.domain.exception.ContactEmailDuplicateException
import com.dobby.domain.gateway.member.MemberGateway
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk

class ValidateContactEmailForSignUpUseCaseTest : BehaviorSpec({

    val memberGateway = mockk<MemberGateway>()
    val validateContactEmailForSignUpUseCase = ValidateContactEmailForSignUpUseCase(memberGateway)

    given("회원 가입 시 이메일 중복 확인") {

        `when`("이메일이 데이터베이스에 존재하지 않는 경우") {
            val input = ValidateContactEmailForSignUpUseCase.Input(contactEmail = "newuser@example.com")

            every { memberGateway.existsByContactEmail(input.contactEmail) } returns false

            then("이메일 사용 가능 응답을 반환해야 한다") {
                val result = validateContactEmailForSignUpUseCase.execute(input)
                result.success shouldBe true
            }
        }

        `when`("이메일이 이미 존재하는 경우") {
            val input = ValidateContactEmailForSignUpUseCase.Input(contactEmail = "existinguser@example.com")

            every { memberGateway.existsByContactEmail(input.contactEmail) } returns true

            then("ContactEmailDuplication 예외가 발생해야 한다") {
                shouldThrow<ContactEmailDuplicateException> {
                    validateContactEmailForSignUpUseCase.execute(input)
                }
            }
        }
    }
})
