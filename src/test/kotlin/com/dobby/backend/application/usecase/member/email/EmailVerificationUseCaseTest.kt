package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.domain.exception.CodeExpiredException
import com.dobby.backend.domain.exception.CodeNotCorrectException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.model.Verification
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class EmailVerificationUseCaseTest : BehaviorSpec({
    val verificationGateway: VerificationGateway = mockk()
    val emailVerificationUseCase = EmailVerificationUseCase(verificationGateway)

    Given("유효한 인증 코드가 주어졌을 때") {
        val validEmail = "christer10@ewhain.net"
        val validCode = "123456"
        val verificationInfo = mockk<Verification> {
            every { verifyCode(validCode) } returns true
            every { isExpired() } returns false
            every { complete() } returns this
        }

        every { verificationGateway.findByUnivEmail(validEmail) } returns verificationInfo
        every { verificationGateway.save(any()) } returns mockk<Verification>()

        When("이메일 인증을 요청하면") {
            val output = emailVerificationUseCase.execute(EmailVerificationUseCase.Input(validEmail, validCode))

            Then("이메일 인증이 성공적으로 완료되어야 한다") {
                output.isSuccess shouldBe true
                output.message shouldBe "학교 메일 인증이 완료되었습니다."
            }
        }
    }

    Given("존재하지 않는 이메일이 주어졌을 때") {
        val invalidEmail = "invalid@univ.edu"
        every { verificationGateway.findByUnivEmail(invalidEmail) } returns null

        When("이메일 인증을 요청하면") {
            Then("VerifyInfoNotFoundException이 발생해야 한다") {
                shouldThrow<VerifyInfoNotFoundException> {
                    emailVerificationUseCase.execute(EmailVerificationUseCase.Input(invalidEmail, "123456"))
                }
            }
        }
    }

    Given("잘못된 인증 코드가 주어졌을 때") {
        val email = "student@univ.edu"
        val incorrectCode = "654321"
        val verificationInfo = mockk<Verification> {
            every { verifyCode(incorrectCode) } returns false
        }
        every { verificationGateway.findByUnivEmail(email) } returns verificationInfo

        When("이메일 인증을 요청하면") {
            Then("CodeNotCorrectException이 발생해야 한다") {
                shouldThrow<CodeNotCorrectException> {
                    emailVerificationUseCase.execute(EmailVerificationUseCase.Input(email, incorrectCode))
                }
            }
        }
    }

    Given("만료된 인증 코드가 주어졌을 때") {
        val email = "student@univ.edu"
        val expiredCode = "123456"
        val verificationInfo = mockk<Verification> {
            every { verifyCode(expiredCode) } returns true
            every { isExpired() } returns true
        }
        every { verificationGateway.findByUnivEmail(email) } returns verificationInfo

        When("이메일 인증을 요청하면") {
            Then("CodeExpiredException이 발생해야 한다") {
                shouldThrow<CodeExpiredException> {
                    emailVerificationUseCase.execute(EmailVerificationUseCase.Input(email, expiredCode))
                }
            }
        }
    }
})


