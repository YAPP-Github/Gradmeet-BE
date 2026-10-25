package com.dobby.external.gateway.email

import com.dobby.config.properties.SESProperties
import com.dobby.exception.EmailSendFailedException
import com.dobby.gateway.email.EmailGateway
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.Body
import software.amazon.awssdk.services.ses.model.Content
import software.amazon.awssdk.services.ses.model.Destination
import software.amazon.awssdk.services.ses.model.Message
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import software.amazon.awssdk.services.ses.model.SesException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Component
class EmailGatewayImpl(
    private val sesAsyncClient: SesAsyncClient,
    private val sesProperties: SESProperties
) : EmailGateway {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * SES 발송 결과를 호출자에게 전파한다.
     *
     * 발송 실패를 콜백 안에서 throw 하면 호출자에게 전달되지 않고 사라지므로,
     * future 를 끝까지 기다린 뒤 예외로 변환한다.
     * 이렇게 해야 호출 측의 RetryUtils 재시도와 상위 예외 핸들러의 알림이 동작한다.
     */
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

        try {
            val response = sesAsyncClient.sendEmail(request).get(SEND_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            logger.info("SES 이메일 발송 완료: to={}, messageId={}", maskEmail(to), response.messageId())
        } catch (ex: ExecutionException) {
            throw toSendFailure(to, ex.cause ?: ex)
        } catch (ex: TimeoutException) {
            throw toSendFailure(to, ex)
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
            throw toSendFailure(to, ex)
        }
    }

    private fun toSendFailure(to: String, cause: Throwable): EmailSendFailedException {
        val maskedTo = maskEmail(to)
        val message = when (cause) {
            is SesException -> {
                val errorDetails = cause.awsErrorDetails()
                "AWS SES 오류 발생: to=$maskedTo, errorCode=${errorDetails?.errorCode()}, errorMessage=${errorDetails?.errorMessage()}"
            }

            is TimeoutException -> "이메일 전송 타임아웃(${SEND_TIMEOUT_SECONDS}초): to=$maskedTo"
            else -> "이메일 전송 실패: to=$maskedTo, ${cause.message}"
        }

        logger.error(message, cause)
        return EmailSendFailedException(message, cause)
    }

    /**
     * 로그에 수신자 주소가 그대로 남지 않도록 로컬 파트를 마스킹한다. (SensitiveDataMasker 와 동일 정책)
     * 배달 실패 추적에는 도메인이 필요하므로 도메인은 남긴다.
     */
    private fun maskEmail(email: String): String {
        if (!email.contains("@")) return "*****"
        return email.first() + "*****@" + email.substringAfter("@")
    }

    companion object {
        private const val SEND_TIMEOUT_SECONDS = 10L
    }
}
