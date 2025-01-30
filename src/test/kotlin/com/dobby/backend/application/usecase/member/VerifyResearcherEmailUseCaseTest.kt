package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.exception.EmailNotValidateException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk

class VerifyResearcherEmailUseCaseTest : BehaviorSpec ({

    val verificationGateway : VerificationGateway = mockk()
    val verifyResearcherUseCase = VerifyResearcherEmailUseCase(verificationGateway)

    given("존재하는 이메일이 주어졌을 때") {
        val validEmail = "christer10@ewhain.net"
        val verifiedInfo = mockk<Verification> {
            every { status  } returns VerificationStatus.VERIFIED
        }

        every { verificationGateway.findByUnivEmail(validEmail) } returns verifiedInfo

        `when`("useCase의 execute가 실행되고, 이메일 검증을 실행하면") {
            then("예외가 발생하지 않는다") {
                verifyResearcherUseCase.execute(validEmail)
            }
        }
    }

    given("존재하지 않는 이메일이 주어졌을 때") {
        val invalidEmail = "invalid.yapp.kr"
        every { verificationGateway.findByUnivEmail(invalidEmail) } returns null

        `when`("useCase의 execute가 실행되고, 이메일 검증을 실행하면") {
            then("VerifyInfoNotFoundException 예외가 발생해야 한다") {
                shouldThrow<VerifyInfoNotFoundException> {
                    verifyResearcherUseCase.execute(invalidEmail)
                }
            }
        }
    }

    given("이메일이 존재하지만 검증되지 않은 상태일 때") {
        val unverifiedEmail = "unverified@ewhain.net"
        val unverifiedInfo = mockk<Verification> {
            every { status } returns VerificationStatus.HOLD
        }

        every { verificationGateway.findByUnivEmail(unverifiedEmail) } returns unverifiedInfo

        `when`("useCase의 execute가 실행되면") {
            then("EmailNotValidateException 예외가 발생해야 한다") {
                shouldThrow<EmailNotValidateException> {
                    verifyResearcherUseCase.execute(unverifiedEmail)
                }
            }
        }
    }
})
