package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.service.CoroutineDispatcherProvider
import com.dobby.backend.application.service.TransactionExecutor
import com.dobby.backend.domain.exception.EmailDomainNotFoundException
import com.dobby.backend.domain.exception.EmailNotUnivException
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.domain.gateway.CacheGateway
import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.util.EmailUtils
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import kotlinx.coroutines.test.runTest

class SendEmailCodeUseCaseTest : BehaviorSpec({

    val verificationGateway: VerificationGateway = mockk(relaxed = true)
    val emailGateway: EmailGateway = mockk(relaxed = true)
    val cacheGateway: CacheGateway = mockk(relaxed = true)
    val idGenerator: IdGenerator = mockk(relaxed = true)
    val dispatcherProvider: CoroutineDispatcherProvider = mockk(relaxed = true)
    val transactionExecutor: TransactionExecutor = mockk(relaxed = true)

    val sendEmailCodeUseCase = SendEmailCodeUseCase(verificationGateway, emailGateway, cacheGateway, idGenerator, dispatcherProvider, transactionExecutor)

    beforeSpec {
        mockkObject(EmailUtils)
    }

    given("유효하지 않은 도메인의 이메일이 주어졌을 때") {
        val invalidEmail = "invalid@unknown.com"
        every { EmailUtils.isDomainExists(invalidEmail) } returns false

        `when`("이메일 인증 코드 전송을 실행하면") {
            then("EmailDomainNotFoundException 예외가 발생해야 한다") {
                runTest {
                    shouldThrow<EmailDomainNotFoundException> {
                        sendEmailCodeUseCase.execute(SendEmailCodeUseCase.Input(invalidEmail))
                    }
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
                runTest {
                    shouldThrow<EmailNotUnivException> {
                        sendEmailCodeUseCase.execute(SendEmailCodeUseCase.Input(personalEmail))
                    }
                }
            }
        }
    }
})


