package com.dobby.backend.application.usecase.signupUseCase.email

import com.dobby.backend.domain.exception.CodeExpiredException
import com.dobby.backend.domain.exception.CodeNotCorrectException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.infrastructure.database.repository.VerificationRepository
import com.dobby.backend.presentation.api.dto.request.signup.EmailVerificationRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime

class EmailVerificationUseCaseTest : BehaviorSpec({

    val mockVerificationRepository = mockk<VerificationRepository>()
    val emailVerificationUseCase = EmailVerificationUseCase(mockVerificationRepository)

    given("유효한 인증 요청이 주어졌을 때") {
        val request = EmailVerificationRequest(univEmail = "test@postech.edu", inputCode = "123456")
        val mockEntity = VerificationEntity(
            id = 1L,
            univMail = "test@postech.edu",
            verificationCode = "123456",
            status = VerificationStatus.HOLD,
            expiresAt = LocalDateTime.now().plusMinutes(10)
        )

        coEvery { mockVerificationRepository.findByUnivMailAndStatus(request.univEmail, VerificationStatus.HOLD) } returns mockEntity
        coEvery { mockVerificationRepository.save(any()) } returns mockEntity

        `when`("EmailVerificationUseCase가 실행되면") {
            val result = emailVerificationUseCase.execute(request)

            then("정상적으로 EmailVerificationResponse를 반환해야 한다") {
                result.isSuccess shouldBe true
            }

            then("인증 상태가 VERIFIED로 업데이트되어야 한다") {
                coVerify {
                    mockVerificationRepository.save(withArg {
                        it.status shouldBe VerificationStatus.VERIFIED
                    })
                }
            }
        }
    }

    given("존재하지 않는 인증 정보가 주어졌을 때") {
        val request = EmailVerificationRequest(univEmail = "test@postech.edu", inputCode = "123456")

        coEvery { mockVerificationRepository.findByUnivMailAndStatus(request.univEmail, VerificationStatus.HOLD) } returns null

        `when`("EmailVerificationUseCase가 실행되면") {
            then("VerifyInfoNotFoundException 예외가 발생해야 한다") {
                val exception = shouldThrow<VerifyInfoNotFoundException> {
                    emailVerificationUseCase.execute(request)
                }
            }
        }
    }

    given("유효하지 않은 인증 코드가 주어졌을 때") {
        val request = EmailVerificationRequest(univEmail = "test@postech.edu", inputCode = "wrong-code")
        val mockEntity = VerificationEntity(
            id = 1L,
            univMail = "test@postech.edu",
            verificationCode = "123456",
            status = VerificationStatus.HOLD,
            expiresAt = LocalDateTime.now().plusMinutes(10)
        )

        coEvery { mockVerificationRepository.findByUnivMailAndStatus(request.univEmail, VerificationStatus.HOLD) } returns mockEntity

        `when`("EmailVerificationUseCase가 실행되면") {
            then("CodeNotCorrectException 예외가 발생해야 한다") {
                val exception = shouldThrow<CodeNotCorrectException> {
                    emailVerificationUseCase.execute(request)
                }
            }
        }
    }

    given("인증 코드가 만료된 경우") {
        val request = EmailVerificationRequest(univEmail = "test@postech.edu", inputCode = "123456")
        val mockEntity = VerificationEntity(
            id = 1L,
            univMail = "test@postech.edu",
            verificationCode = "123456",
            status = VerificationStatus.HOLD,
            expiresAt = LocalDateTime.now().minusMinutes(1)
        )

        coEvery { mockVerificationRepository.findByUnivMailAndStatus(request.univEmail, VerificationStatus.HOLD) } returns mockEntity

        `when`("EmailVerificationUseCase가 실행되면") {
            then("CodeExpiredException 예외가 발생해야 한다") {
                val exception = shouldThrow<CodeExpiredException> {
                    emailVerificationUseCase.execute(request)
                }
            }
        }
    }
})

