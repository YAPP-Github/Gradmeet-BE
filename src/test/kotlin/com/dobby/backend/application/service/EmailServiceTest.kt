package com.dobby.backend.application.service
import com.dobby.backend.application.usecase.member.email.EmailCodeSendUseCase
import com.dobby.backend.application.usecase.member.email.EmailVerificationUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class EmailServiceTest : BehaviorSpec({

    val emailCodeSendUseCase: EmailCodeSendUseCase = mockk()
    val emailVerificationUseCase: EmailVerificationUseCase = mockk()

    val emailService = EmailService(
        emailCodeSendUseCase = emailCodeSendUseCase,
        emailVerificationUseCase = emailVerificationUseCase
    )

    given("EmailCodeSendUseCase.Input과 실행 결과가 주어졌을 때") {
        val input = EmailCodeSendUseCase.Input(univEmail = "test@postech.edu")
        val expectedOutput = EmailCodeSendUseCase.Output(
            isSuccess = true,
            message = "해당 학교 이메일로 성공적으로 코드를 전송했습니다. 10분 이내로 인증을 완료해주세요."
        )

        every { emailCodeSendUseCase.execute(input) } returns expectedOutput

        `when`("sendEmail 메서드를 호출하면") {
            val result = emailService.sendEmail(input)

            then("EmailCodeSendUseCase의 execute가 호출되고 결과를 반환해야 한다") {
                result shouldBe expectedOutput
                verify(exactly = 1) { emailCodeSendUseCase.execute(input) }
            }
        }
    }

    given("EmailVerificationUseCase.Input과 실행 결과가 주어졌을 때") {
        val input = EmailVerificationUseCase.Input(
            univEmail = "test@postech.edu",
            inputCode = "123456"
        )
        val expectedOutput = EmailVerificationUseCase.Output(
            isSuccess = true,
            message = "학교 메일 인증이 완료되었습니다."
        )

        every { emailVerificationUseCase.execute(input) } returns expectedOutput

        `when`("verifyCode 메서드를 호출하면") {
            val result = emailService.verifyCode(input)

            then("EmailVerificationUseCase의 execute가 호출되고 결과를 반환해야 한다") {
                result shouldBe expectedOutput
                verify(exactly = 1) { emailVerificationUseCase.execute(input) }
            }
        }
    }
})
