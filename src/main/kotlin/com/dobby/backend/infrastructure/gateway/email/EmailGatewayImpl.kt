package com.dobby.backend.infrastructure.gateway.email

import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.infrastructure.config.properties.EmailProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class EmailGatewayImpl(
    private val emailProperties: EmailProperties,
    private val mailSender: JavaMailSender
) : EmailGateway {

    override suspend fun sendEmail(to: String, subject: String, content: String, isHtml: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                val message = mailSender.createMimeMessage()
                val helper = MimeMessageHelper(message, false, "UTF-8")

                helper.setTo(to)
                helper.setSubject(subject)
                helper.setText(content, isHtml)

                mailSender.send(message)
            } catch (ex: Exception) {
                throw IllegalStateException("이메일 발송 실패: ${ex.message}")
            }
        }
    }
}

