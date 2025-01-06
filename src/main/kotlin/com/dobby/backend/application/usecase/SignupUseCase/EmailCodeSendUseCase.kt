package com.dobby.backend.application.usecase.SignupUseCase

import com.dobby.backend.application.mapper.VerificationMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.gateway.EmailGateway
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.infrastructure.database.repository.VerificationRepository
import com.dobby.backend.presentation.api.dto.request.signup.EmailSendRequest
import com.dobby.backend.presentation.api.dto.response.signup.EmailSendResponse
import java.time.LocalDateTime
import java.util.Hashtable
import javax.naming.directory.InitialDirContext
import javax.naming.directory.Attributes

class EmailCodeSendUseCase(
    private val verificationRepository: VerificationRepository,
    private val emailGateway: EmailGateway
) : UseCase<EmailSendRequest, EmailSendResponse> {
    override fun execute(input: EmailSendRequest): EmailSendResponse {
        if(!isDomainExists(input.univEmail)) throw EmailDomainNotFoundException()
        if(!isUnivMail(input.univEmail)) throw EmailNotUnivException()

        val existingInfo = verificationRepository.findByUnivMail(input.univEmail)
        val code = generateCode()

        if(existingInfo != null) {
            when (existingInfo.status) {
                VerificationStatus.HOLD -> {
                    existingInfo.verificationCode = code
                    existingInfo.status = VerificationStatus.HOLD
                    existingInfo.expiresAt = LocalDateTime.now().plusMinutes(10)
                    verificationRepository.save(existingInfo)
                }

                VerificationStatus.VERIFIED -> {
                    throw EmailAlreadyVerifiedException()
                }
            }
        }
        else {
            val newVerificationInfo = VerificationMapper.toEntity(input, code)
            verificationRepository.save(newVerificationInfo)
        }


        val subject= "그라밋 - 이메일 인증 코드 입니다."
        val content = """
            안녕하세요, 그라밋입니다.
            
            아래의 코드는 이메일 인증을 위한 코드입니다:
            
            $code
            
            10분 이내에 인증을 완료해주세요.
        """.trimIndent()
        emailGateway.sendEmail(input.univEmail, subject, content)
        return VerificationMapper.toSendResDto()
    }

    private fun extractDomain(email:String): String {
        if(!email.contains("@")) throw EmailFormatInvalidException()
        return email.substringAfter("@")
    }
    private fun isDomainExists(email: String): Boolean {
        val domain = extractDomain(email)
        return try {
            val env = Hashtable<String, String>()
            env["java.naming.factory.initial"] = "com.sun.jndi.dns.DnsContextFactory"
            val ctx = InitialDirContext(env)
            val attributes: Attributes = ctx.getAttributes(domain, arrayOf("MX"))
            val mxRecords = attributes.get("MX")
            println("MX Records for $domain: $mxRecords")
            mxRecords != null
        } catch (ex: EmailDomainNotFoundException) {
            println("DNS lookup failed for $domain: ${ex.message}")
            false
        }
    }


    private fun isUnivMail(email: String): Boolean {
        val eduDomains = setOf(
            "postech.edu",
            "kaist.edu",
            "handong.edu",
            "ewhain.net"
        )
        return email.endsWith("@ac.kr") || eduDomains.any {email.endsWith(it)}
    }

    private fun generateCode() : String {
        val randomNum = (0..999999).random()
        return String.format("%06d", randomNum)
    }

}
