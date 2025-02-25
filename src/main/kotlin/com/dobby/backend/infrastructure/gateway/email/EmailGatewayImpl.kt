package com.dobby.backend.infrastructure.gateway.email

import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.infrastructure.config.properties.SESProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.*

@Component
class EmailGatewayImpl(
    private val sesClient: SesClient,
    private val sesProperties: SESProperties
) : EmailGateway {

    override suspend fun sendEmail(to: String, subject: String, content: String, isHtml: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                val body = if(isHtml) {
                    Body.builder().html(Content.builder().data(content).build()).build()
                } else {
                    Body.builder().text(Content.builder().data(content).build()).build()
                }

                val request = SendEmailRequest.builder()
                    .source(sesProperties.email.sender)
                    .destination(Destination.builder().toAddresses(to).build())
                    .message(
                        Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(body)
                            .build()
                    )
                    .build()

                sesClient.sendEmail(request)

            } catch (ex: Exception) {
                throw IllegalStateException("이메일 발송 실패: ${ex.message}")
            }
        }
    }
}
