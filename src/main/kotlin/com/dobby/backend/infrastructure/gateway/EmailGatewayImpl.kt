package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.EmailGateway
import com.dobby.backend.infrastructure.config.properties.EmailProperties
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailGatewayImpl(
    private val emailProperties: EmailProperties,
    private val mailSender: JavaMailSender
) : EmailGateway {

    override fun sendEmail(to: String, subject: String, content: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = subject
        message.text = content

        try {
            mailSender.send(message)
        } catch (ex: Exception) {
            throw IllegalStateException("이메일 발송 실패: ${ex.message}")
        }
    }
}

