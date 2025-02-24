package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.domain.exception.CodeExpiredException
import com.dobby.backend.domain.exception.CodeNotCorrectException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.domain.gateway.CacheGateway
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.domain.enums.VerificationStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class VerifyEmailUseCaseTest : BehaviorSpec({
    val verificationGateway: VerificationGateway = mockk(relaxed = true)
    val cacheGateway: CacheGateway = mockk(relaxed = true)
    val verifyEmailUseCase = VerifyEmailUseCase(verificationGateway, cacheGateway)

    given("유효한 인증 코드가 주어졌을 때") {
        val validEmail = "christer10@ewhain.net"
        val validCode = "123456"
        val verificationInfo = mockk<Verification> {
            every { status } returns VerificationStatus.HOLD
            every { complete() } returns this
        }

        every { verificationGateway.findByUnivEmail(validEmail) } returns verificationInfo
        every { verificationGateway.save(any()) } returns mockk<Verification>()
        every { cacheGateway.get("verification:$validEmail") } returns validCode

        `when`("이메일 인증을 요청하면") {
            val output = verifyEmailUseCase.execute(VerifyEmailUseCase.Input(validEmail, validCode))

            then("이메일 인증이 성공적으로 완료되어야 한다") {
                output.isSuccess shouldBe true
                output.message shouldBe "학교 메일 인증이 완료되었습니다."
            }
        }
    }

    given("잘못된 인증 코드가 주어졌을 때") {
        val email = "student@univ.edu"
        val incorrectCode = "654321"
        val verificationInfo = mockk<Verification> {
        }
        every { verificationGateway.findByUnivEmail(email) } returns verificationInfo
        every { cacheGateway.get("verification:$email") } returns "123456" // 정상 코드 설정

        `when`("이메일 인증을 요청하면") {
            then("CodeNotCorrectException이 발생해야 한다") {
                shouldThrow<CodeNotCorrectException> {
                    verifyEmailUseCase.execute(VerifyEmailUseCase.Input(email, incorrectCode))
                }
            }
        }
    }
})



