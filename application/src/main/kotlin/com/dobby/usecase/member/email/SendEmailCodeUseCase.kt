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
import com.dobby.model.Verification
import com.dobby.usecase.UseCase
import com.dobby.util.EmailUtils
import com.dobby.util.IdGenerator
import com.dobby.util.RetryUtils

class SendEmailCodeUseCase(
    private val verificationGateway: VerificationGateway,
    private val researcherGateway: ResearcherGateway,
    private val emailGateway: EmailGateway,
    private val cacheGateway: CacheGateway,
    private val idGenerator: IdGenerator,
    private val emailTemplateLoader: EmailTemplateLoader
) : UseCase<SendEmailCodeUseCase.Input, SendEmailCodeUseCase.Output> {

    data class Input(
        val univEmail: String
    )
    data class Output(
        val isSuccess: Boolean,
        val message: String,
        val requestCount: Int
    )
    override fun execute(input: Input): Output {
        validateEmail(input.univEmail)

        val requestCountKey = "request_count:${input.univEmail}"
        val currentCount = cacheGateway.get(requestCountKey)?.toIntOrNull() ?: 0
        if (currentCount >= 3) {
            throw TooManyVerificationRequestException
        }

        cacheGateway.incrementRequestCount(requestCountKey)
        val code = EmailUtils.generateCode()
        RetryUtils.retryWithBackOff {
            reflectVerification(input.univEmail, code)
        }

        sendVerificationEmail(input.univEmail, code)

        return Output(
            isSuccess = true,
            message = "학교 이메일로 코드가 전송되었습니다. 10분 이내로 인증을 완료해주세요.",
            requestCount = cacheGateway.get(requestCountKey)?.toIntOrNull() ?: 0
        )
    }

    private fun validateEmail(email: String) {
        if (!EmailUtils.isDomainExists(email)) throw EmailDomainNotFoundException
        if (!EmailUtils.isUnivMail(email)) throw EmailNotUnivException
        if (researcherGateway.existsByUnivEmail(email)) {
            throw SignupUnivEmailDuplicateException
        }
        if (verificationGateway.findByUnivEmailAndStatus(email, VerificationStatus.VERIFIED) != null) {
            throw EmailAlreadyVerifiedException
        }
    }

    private fun reflectVerification(univEmail: String, code: String) {
        try {
            val existingInfo = verificationGateway.findByUnivEmailAndStatus(univEmail, VerificationStatus.HOLD)

            if (existingInfo != null) {
                val updatedVerification = existingInfo.update()
                verificationGateway.save(updatedVerification)
            } else {
                val newVerification = Verification.newVerification(
                    id = idGenerator.generateId(),
                    univEmail = univEmail
                )
                verificationGateway.save(newVerification)
            }
        } catch (e: Exception) {
            return
        }
        cacheGateway.setCode("verification:$univEmail", code)
    }

    companion object {
        private const val EMAIL_SUBJECT = "[그라밋] 학교 메일 인증 코드가 도착했어요."
    }

    private fun sendVerificationEmail(univEmail: String, code: String) {
        val content = emailTemplateLoader.loadVerificationTemplate(code)
        RetryUtils.retryWithBackOff {
            emailGateway.sendEmail(univEmail, EMAIL_SUBJECT, content, isHtml = true)
        }
    }
}
