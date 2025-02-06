package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.domain.exception.EmailAlreadyVerifiedException
import com.dobby.backend.domain.exception.EmailDomainNotFoundException
import com.dobby.backend.domain.exception.EmailNotUnivException
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import com.dobby.backend.util.EmailUtils
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class EmailCodeSendUseCaseTest : BehaviorSpec({

    val verificationGateway: VerificationGateway = mockk()
    val emailGateway: EmailGateway = mockk()
    val idGenerator: IdGenerator = mockk()

    val sendEmailCodeUseCase = SendEmailCodeUseCase(verificationGateway, emailGateway, idGenerator)

    beforeSpec {
        mockkObject(EmailUtils)
    }

        given("유효한 대학 이메일이 주어졌을 때") {
            val univEmail = "christer10@ewhain.net"
            val verificationCode = "123456"

            every { EmailUtils.isDomainExists(univEmail) } returns true
            every { EmailUtils.isUnivMail(univEmail) } returns true
            every { EmailUtils.generateCode() } returns verificationCode
            every { verificationGateway.findByUnivEmail(univEmail) } returns null
            every { idGenerator.generateId() } returns "1"
            every { verificationGateway.save(any()) } returns mockk<Verification>()
            every { emailGateway.sendEmail(any(), any(), any()) } just Runs

            `when`("이메일 인증 코드 전송을 실행하면") {
                val output = sendEmailCodeUseCase.execute(SendEmailCodeUseCase.Input(univEmail))

                then("성공적으로 이메일이 전송되었음을 반환해야 한다") {
                    output.isSuccess shouldBe true
                    output.message shouldBe "학교 이메일로 코드가 전송되었습니다. 10분 이내로 인증을 완료해주세요."
                }
            }
        }

        given("유효하지 않은 도메인의 이메일이 주어졌을 때") {
            val invalidEmail = "invalid@unknown.com"
            every { EmailUtils.isDomainExists(invalidEmail) } returns false

            `when`("이메일 인증 코드 전송을 실행하면") {
                then("EmailDomainNotFoundException 예외가 발생해야 한다") {
                    shouldThrow<EmailDomainNotFoundException> {
                        sendEmailCodeUseCase.execute(SendEmailCodeUseCase.Input(invalidEmail))
                    }
                }
            }
        }

        given("대학 이메일이 아닌 일반 이메일이 주어졌을 때") {
            val personalEmail = "christer10@naver.com"
            every { EmailUtils.isDomainExists(personalEmail) } returns true
            every { EmailUtils.isUnivMail(personalEmail) } returns false

            `when`("이메일 인증 코드 전송을 실행하면") {
                then("EmailNotUnivException 예외가 발생해야 한다") {
                    shouldThrow<EmailNotUnivException> {
                        sendEmailCodeUseCase.execute(SendEmailCodeUseCase.Input(personalEmail))
                    }
                }
            }
        }

        given("이미 검증된 이메일이 주어졌을 때") {
            val verifiedEmail = "christer10@ewhain.net"
            val verifiedInfo = mockk<Verification> {
                every { status } returns VerificationStatus.VERIFIED
            }

            every { EmailUtils.isDomainExists(verifiedEmail) } returns true
            every { EmailUtils.isUnivMail(verifiedEmail) } returns true
            every { verificationGateway.findByUnivEmail(verifiedEmail) } returns verifiedInfo

            `when`("이메일 인증 코드 전송을 실행하면") {
                then("EmailAlreadyVerifiedException 예외가 발생해야 한다") {
                    shouldThrow<EmailAlreadyVerifiedException> {
                        sendEmailCodeUseCase.execute(SendEmailCodeUseCase.Input(verifiedEmail))
                    }
                }
            }
        }
})


