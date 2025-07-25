package com.dobby.usecase.member.email

import com.dobby.EmailTemplateLoader
import com.dobby.enums.VerificationStatus
import com.dobby.exception.EmailAlreadyVerifiedException
import com.dobby.exception.EmailDomainNotFoundException
import com.dobby.exception.EmailNotUnivException
import com.dobby.exception.SignupUnivEmailDuplicateException
import com.dobby.exception.TooManyVerificationRequestException
import com.dobby.gateway.CacheGateway
import com.dobby.gateway.email.EmailGateway
import com.dobby.gateway.email.VerificationGateway
import com.dobby.gateway.member.ResearcherGateway
import com.dobby.util.EmailUtils
import com.dobby.util.IdGenerator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest

class SendEmailCodeUseCaseTest : BehaviorSpec({

    val verificationGateway: VerificationGateway = mockk(relaxed = true)
    val researcherGateway: ResearcherGateway = mockk(relaxed = true)
    val emailGateway: EmailGateway = mockk(relaxed = true)
    val cacheGateway: CacheGateway = mockk(relaxed = true)
    val idGenerator: IdGenerator = mockk(relaxed = true)
    val emailTemplateLoader: EmailTemplateLoader = mockk(relaxed = true)

    val sendEmailCodeUseCase = SendEmailCodeUseCase(verificationGateway, researcherGateway, emailGateway, cacheGateway, idGenerator, emailTemplateLoader)

    beforeSpec {
        mockkObject(EmailUtils)
    }

    afterSpec {
        unmockkObject(EmailUtils)
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

    given("해외 대학 이메일 도메인이 주어졌을 때") {
        val overseasEmail = "dobby-socks@griffithuni.edu.au"
        every { EmailUtils.isDomainExists(overseasEmail) } returns true
        every { EmailUtils.isUnivMail(overseasEmail) } returns true
        every { researcherGateway.existsByUnivEmail(overseasEmail) } returns false
        every { verificationGateway.findByUnivEmailAndStatus(overseasEmail, VerificationStatus.VERIFIED) } returns null
        every { cacheGateway.get("request_count:$overseasEmail") } returns null

        `when`("이메일 인증 코드 전송을 실행하면") {
            then("예외 없이 정상 동작해야 한다") {
                runTest {
                    val input = SendEmailCodeUseCase.Input(overseasEmail)
                    val output = sendEmailCodeUseCase.execute(input)
                    output.isSuccess shouldBe true
                    coVerify(exactly = 1) {
                        emailGateway.sendEmail(overseasEmail, any(), any(), true)
                    }
                }
            }
        }
    }

    given("이미 가입된 대학 이메일이 주어졌을 때") {
        val duplicateEmail = "christer10@ewhain.net"
        every { EmailUtils.isDomainExists(duplicateEmail) } returns true
        every { EmailUtils.isUnivMail(duplicateEmail) } returns true
        every { researcherGateway.existsByUnivEmail(duplicateEmail) } returns true

        `when`("이메일 인증 코드 전송을 실행하면") {
            then("SignupUnivEmailDuplicateException 예외가 발생해야 한다") {
                shouldThrow<SignupUnivEmailDuplicateException> {
                    sendEmailCodeUseCase.execute(SendEmailCodeUseCase.Input(duplicateEmail))
                }
            }
        }
    }

    given("회원가입은 완료하지 않았지만, 이미 인증된 대학 이메일이 주어졌을 때") {
        val verifiedEmail = "verified@ewhain.net"
        every { EmailUtils.isDomainExists(verifiedEmail) } returns true
        every { EmailUtils.isUnivMail(verifiedEmail) } returns true
        every { researcherGateway.existsByUnivEmail(verifiedEmail) } returns false
        every { verificationGateway.findByUnivEmailAndStatus(verifiedEmail, VerificationStatus.VERIFIED) } returns mockk()

        `when`("이메일 인증 코드 전송을 실행하면") {
            then("EmailAlreadyVerifiedException 예외가 발생해야 한다") {
                shouldThrow<EmailAlreadyVerifiedException> {
                    sendEmailCodeUseCase.execute(SendEmailCodeUseCase.Input(verifiedEmail))
                }
            }
        }
    }

    given("이메일 인증 코드 요청 횟수가 초과된 경우") {
        val limitedEmail = "limited@ewhain.net"
        every { EmailUtils.isDomainExists(limitedEmail) } returns true
        every { EmailUtils.isUnivMail(limitedEmail) } returns true
        every { researcherGateway.existsByUnivEmail(limitedEmail) } returns false
        every { verificationGateway.findByUnivEmailAndStatus(limitedEmail, VerificationStatus.VERIFIED) } returns null
        every { cacheGateway.get("request_count:$limitedEmail") } returns "3"

        `when`("이메일 인증 코드 전송을 실행하면") {
            then("TooManyVerificationRequestException 예외가 발생해야 한다") {
                shouldThrow<TooManyVerificationRequestException> {
                    sendEmailCodeUseCase.execute(SendEmailCodeUseCase.Input(limitedEmail))
                }
            }
        }
    }
})
