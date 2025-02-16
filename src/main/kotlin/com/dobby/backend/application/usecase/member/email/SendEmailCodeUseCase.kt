package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.service.CoroutineDispatcherProvider
import com.dobby.backend.application.service.TransactionExecutor
import com.dobby.backend.application.usecase.AsyncUseCase
import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.domain.gateway.CacheGateway
import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import com.dobby.backend.util.EmailUtils
import com.dobby.backend.util.RetryUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class SendEmailCodeUseCase(
    private val verificationGateway: VerificationGateway,
    private val emailGateway: EmailGateway,
    private val cacheGateway: CacheGateway,
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

        val requestCountKey = "request_count:${input.univEmail}"
        val currentCount = cacheGateway.get(requestCountKey)?.toIntOrNull() ?: 0
        if(currentCount >= 3) {
            throw TooManyVerificationRequestException
        }

        cacheGateway.incrementRequestCount(requestCountKey)
        val code = EmailUtils.generateCode()
        CoroutineScope(dispatcherProvider.io).launch {
            RetryUtils.retryWithBackOff {
                transactionExecutor.execute {
                    reflectVerification(input.univEmail, code)
                }
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
        if(verificationGateway.findByUnivEmailAndStatus(email, VerificationStatus.VERIFIED) != null)
            throw EmailAlreadyVerifiedException
    }

    private fun reflectVerification(univEmail: String, code: String) {
        val existingInfo = verificationGateway.findByUnivEmailAndStatus(univEmail, VerificationStatus.HOLD)

        if(existingInfo != null) {
            cacheGateway.setCode("verification:${univEmail}", code)
            val updatedVerification = existingInfo.update()
            verificationGateway.save(updatedVerification)
        }
        else {
            val newVerification = Verification.newVerification(
                id = idGenerator.generateId(),
                univEmail = univEmail
            )
            verificationGateway.save(newVerification)
            cacheGateway.setCode("verification:${univEmail}", code)
        }
    }

    private suspend fun sendVerificationEmail(univEmail: String, code: String) {
        val content = EMAIL_CONTENT_TEMPLATE.format(code)

        RetryUtils.retryWithBackOff {
            emailGateway.sendEmail(univEmail, EMAIL_SUBJECT, content)
        }
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
