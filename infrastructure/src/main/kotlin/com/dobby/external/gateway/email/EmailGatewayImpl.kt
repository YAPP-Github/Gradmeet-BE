package com.dobby.external.gateway.email

import com.dobby.config.properties.SESProperties
import com.dobby.gateway.email.EmailGateway
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.Body
import software.amazon.awssdk.services.ses.model.Content
import software.amazon.awssdk.services.ses.model.Destination
import software.amazon.awssdk.services.ses.model.Message
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import software.amazon.awssdk.services.ses.model.SesException

@Component
class EmailGatewayImpl(
    private val sesAsyncClient: SesAsyncClient,
    private val sesProperties: SESProperties
) : EmailGateway {

    override fun sendEmail(to: String, subject: String, content: String, isHtml: Boolean) {
        val body = if (isHtml) {
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

        sesAsyncClient.sendEmail(request)
            .whenComplete { _, ex ->
                when (ex) {
                    is SesException -> throw IllegalStateException("AWS SES 오류 발생: ${ex.awsErrorDetails()?.errorMessage()}")
                    is Exception -> throw IllegalStateException("이메일 전송 실패: ${ex.message}")
                }
            }
    }
}
