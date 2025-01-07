package com.dobby.backend.application.usecase.SignupUseCase.email

import com.dobby.backend.domain.exception.EmailNotUnivException
import com.dobby.backend.domain.gateway.EmailGateway
import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.infrastructure.database.repository.VerificationRepository
import com.dobby.backend.presentation.api.dto.request.signup.EmailSendRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.time.LocalDateTime

class EmailCodeSendUseCaseTest : BehaviorSpec({
    val mockVerificationRepository = mockk<VerificationRepository>()
    val mockEmailGateway = mockk<EmailGateway>()

    val emailCodeSendUseCase = EmailCodeSendUseCase(
        verificationRepository = mockVerificationRepository,
        emailGateway = mockEmailGateway
    )

    given("유효한 대학 이메일이 주어졌을 때") {
        val request = EmailSendRequest(univEmail = "test@postech.edu")


        coEvery { mockVerificationRepository.findByUnivMail(request.univEmail) } returns null
        val mockEntity = VerificationEntity(
            id = 1L,
            univMail = "test@postech.edu",
            verificationCode = "123456",
            status = VerificationStatus.HOLD,
            expiresAt = LocalDateTime.now().plusMinutes(10)
        )
        coEvery { mockVerificationRepository.save(any()) } returns mockEntity
        coEvery { mockEmailGateway.sendEmail(any(), any(), any()) } returns Unit


        `when`("emailCodeSendUseCase가 실행되면") {
            val result = emailCodeSendUseCase.execute(request)

            then("정상적으로 EmailSendResponse를 반환해야 한다") {
                result.isSuccess shouldBe true
            }

            then("save 메서드가 호출되어야 한다") {
                coVerify { mockVerificationRepository.save(any()) }
            }

            then("이메일이 발송되어야 한다") {
                coVerify { mockEmailGateway.sendEmail(
                    "test@postech.edu",
                    any(),
                    any()) }
            }
        }
    }

    given("대학 이메일이 아닌 경우") {
        val request = EmailSendRequest(univEmail = "test@gmail.com")

        `when`("코드 전송 요청을 하면") {
            then("EmailNotUnivException 예외가 발생해야 한다") {
                val exception = shouldThrow<EmailNotUnivException> {
                    emailCodeSendUseCase.execute(request)
                }
            }
        }
    }
})

