package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enums.VerificationStatus
import com.dobby.backend.util.EmailUtils

class EmailCodeSendUseCase(
    private val verificationGateway: VerificationGateway,
    private val emailGateway: EmailGateway
) : UseCase<EmailCodeSendUseCase.Input, EmailCodeSendUseCase.Output> {

    data class Input(
        val univEmail: String
    )
    data class Output(
        val isSuccess: Boolean,
        val message: String
    )
    override fun execute(input: Input): Output {
        validateEmail(input.univEmail)

        val code = EmailUtils.generateCode()
        reflectVerification(input, code)

        sendVerificationEmail(input, code)
        return Output(
            isSuccess = true,
            message = "학교 이메일로 코드가 전송되었습니다. 10분 이내로 인증을 완료해주세요."
        )
    }

    private fun validateEmail(email : String){
        if(!EmailUtils.isDomainExists(email)) throw EmailDomainNotFoundException()
        if(!EmailUtils.isUnivMail(email)) throw EmailNotUnivException()
    }

    private fun reflectVerification(input: Input, code: String) {
        val existingInfo = verificationGateway.findByUnivEmail(input.univEmail)

        if (existingInfo != null) {
            if (existingInfo.status == VerificationStatus.VERIFIED) {
                throw EmailAlreadyVerifiedException()
            }
            verificationGateway.updateCode(existingInfo.univEmail, code)
        }
        else {
            val newVerification = Verification.newVerification(
                univEmail = input.univEmail,
                verificationCode = code
            )
            verificationGateway.save(newVerification)
        }
    }

    private fun sendVerificationEmail(input: Input, code: String) {
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
