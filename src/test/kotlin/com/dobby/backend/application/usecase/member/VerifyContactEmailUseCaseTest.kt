package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.exception.ContactEmailDuplicateException
import com.dobby.backend.domain.gateway.member.MemberGateway
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk

class VerifyContactEmailUseCaseTest : BehaviorSpec({

    val memberGateway = mockk<MemberGateway>()
    val verifyContactEmailUseCase = VerifyContactEmailUseCase(memberGateway)

    given("회원 가입 시 이메일 중복 확인") {

        `when`("이메일이 데이터베이스에 존재하지 않는 경우") {
            val input = VerifyContactEmailUseCase.Input(contactEmail = "newuser@example.com")

            every { memberGateway.existsByContactEmail(input.contactEmail) } returns false

            then("이메일 사용 가능 응답을 반환해야 한다") {
                val result = verifyContactEmailUseCase.execute(input)
                result.success shouldBe true
            }
        }

        `when`("이메일이 이미 존재하는 경우") {
            val input = VerifyContactEmailUseCase.Input(contactEmail = "existinguser@example.com")

            every { memberGateway.existsByContactEmail(input.contactEmail) } returns true

            then("ContactEmailDuplication 예외가 발생해야 한다") {
                shouldThrow<ContactEmailDuplicateException> {
                    verifyContactEmailUseCase.execute(input)
                }
            }
        }
    }
})
