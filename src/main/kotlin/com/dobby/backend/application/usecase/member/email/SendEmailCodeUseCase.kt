package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.service.CoroutineDispatcherProvider
import com.dobby.backend.application.service.TransactionExecutor
import com.dobby.backend.application.usecase.AsyncUseCase
import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import com.dobby.backend.util.EmailUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import kotlin.math.pow
import kotlin.random.Random

class SendEmailCodeUseCase(
    private val verificationGateway: VerificationGateway,
    private val emailGateway: EmailGateway,
    private val idGenerator: IdGenerator,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val transactionExecutor: TransactionExecutor
) : AsyncUseCase<SendEmailCodeUseCase.Input, SendEmailCodeUseCase.Output> {

    data class Input(
        val univEmail: String
    )
    data class Output(
        val isSuccess: Boolean,
        val message: String
    )
    override suspend fun execute(input: Input): Output {
        validateEmail(input.univEmail)

        val code = EmailUtils.generateCode()

        CoroutineScope(dispatcherProvider.io).launch {
            transactionExecutor.execute {
                reflectVerification(input, code)
            }
        }
        CoroutineScope(dispatcherProvider.io).launch {
            sendVerificationEmail(input.univEmail, code)
        }

        return Output(
            isSuccess = true,
            message = "학교 이메일로 코드가 전송되었습니다. 10분 이내로 인증을 완료해주세요."
        )
    }

    private fun validateEmail(email : String){
        if(!EmailUtils.isDomainExists(email)) throw EmailDomainNotFoundException
        if(!EmailUtils.isUnivMail(email)) throw EmailNotUnivException
    }

    private fun reflectVerification(input: Input, code: String) {
        val existingInfo = verificationGateway.findByUnivEmail(input.univEmail)

        if (existingInfo != null) {
            if (existingInfo.status == VerificationStatus.VERIFIED) {
                throw EmailAlreadyVerifiedException
            }
            verificationGateway.updateCode(existingInfo.univEmail, code)
            return
        }
        else {
            val newVerification = Verification.newVerification(
                id = idGenerator.generateId(),
                univEmail = input.univEmail,
                verificationCode = code
            )
            verificationGateway.save(newVerification)
        }
    }

    private val logger = LoggerFactory.getLogger(SendEmailCodeUseCase::class.java)

    private suspend fun sendVerificationEmail(univEmail: String, code: String) {
        val content = EMAIL_CONTENT_TEMPLATE.format(code)
        val maxRetries = 3

        var attempt = 1
        while(attempt <= maxRetries) {
            try {
                emailGateway.sendEmail(univEmail, EMAIL_SUBJECT, content)
                return
            } catch (ex: Exception) {
                attempt += 1
                if(attempt >= maxRetries) {
                    logger.error("Failed to send email verification to $univEmail after $maxRetries attempts.", ex)
                    throw ex
                }
                val backOffTime = calculateBackOff(attempt)
                logger.warn("Retrying to sending email... Attempt: $attempt, Waiting: ${backOffTime}ms")
                delay(backOffTime)
            }
        }
    }

    private fun calculateBackOff(attempt: Int): Long {
        val defaultDelay = 1000L
        val maxJitter = 500L
        return defaultDelay * (2.0.pow(attempt)).toLong() + Random.nextLong(0, maxJitter)
    }

    companion object {
        private const val EMAIL_SUBJECT = "그라밋 - 이메일 인증 코드 입니다."
        private const val EMAIL_CONTENT_TEMPLATE = """
            안녕하세요, 그라밋입니다.
            
            아래의 코드는 이메일 인증을 위한 코드입니다:
            
            %s
            
            10분 이내에 인증을 완료해주세요.
        """
    }
}
