package com.dobby.backend.application.usecase.SignupUseCase.email

import com.dobby.backend.application.mapper.VerificationMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.gateway.EmailGateway
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.infrastructure.database.repository.VerificationRepository
import com.dobby.backend.presentation.api.dto.request.signup.EmailSendRequest
import com.dobby.backend.presentation.api.dto.response.signup.EmailSendResponse
import com.dobby.backend.util.EmailUtils
import java.time.LocalDateTime

class EmailCodeSendUseCase(
    private val verificationRepository: VerificationRepository,
    private val emailGateway: EmailGateway
) : UseCase<EmailSendRequest, EmailSendResponse> {
    override fun execute(input: EmailSendRequest): EmailSendResponse {
        validateEmail(input.univEmail)

        val code = EmailUtils.generateCode()
        reflectVerification(input, code)

        sendVerificationEmail(input, code)
        return VerificationMapper.toSendResDto()
    }

    private fun validateEmail(email : String){
        if(!EmailUtils.isDomainExists(email)) throw EmailDomainNotFoundException()
        if(!EmailUtils.isUnivMail(email)) throw EmailNotUnivException()
    }

    private fun reflectVerification(input: EmailSendRequest, code: String) {
        val existingInfo = verificationRepository.findByUnivMail(input.univEmail)

        if (existingInfo != null) {
            when (existingInfo.status) {
                VerificationStatus.HOLD -> {
                    existingInfo.verificationCode = code
                    existingInfo.expiresAt = LocalDateTime.now().plusMinutes(10)
                    verificationRepository.save(existingInfo)
                }

                VerificationStatus.VERIFIED -> {
                    throw EmailAlreadyVerifiedException()
                }
            }
        } else {
            val newVerificationInfo = VerificationMapper.toEntity(input, code)
            verificationRepository.save(newVerificationInfo)
        }
    }

    private fun sendVerificationEmail(input: EmailSendRequest, code: String) {
        val content = EMAIL_CONTENT_TEMPLATE.format(code)
        emailGateway.sendEmail(input.univEmail, EMAIL_SUBJECT, content)
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
