package com.dobby.external.gateway.email

import com.dobby.config.properties.SESProperties
import com.dobby.exception.EmailSendFailedException
import com.dobby.util.RetryUtils
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import software.amazon.awssdk.awscore.exception.AwsErrorDetails
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import software.amazon.awssdk.services.ses.model.SendEmailResponse
import software.amazon.awssdk.services.ses.model.SesException
import java.util.concurrent.CompletableFuture

class EmailGatewayImplTest : BehaviorSpec({

    val sesProperties = SESProperties(
        email = SESProperties.Email(sender = "no-reply@gradmeet.co.kr"),
        region = SESProperties.Region(static = "ap-northeast-2"),
        credentials = SESProperties.Credentials(accessKey = "test-access-key", secretKey = "test-secret-key")
    )
    val recipient = "ssim6@gsu.edu"

    given("SES 발송이 성공하는 경우") {
        `when`("HTML 메일을 발송하면") {
            then("설정된 발신자와 수신자로 요청이 구성되고 예외 없이 완료된다") {
                val sesAsyncClient = mockk<SesAsyncClient>()
                val requestSlot = slot<SendEmailRequest>()
                every { sesAsyncClient.sendEmail(capture(requestSlot)) } returns
                    CompletableFuture.completedFuture(
                        SendEmailResponse.builder().messageId("test-message-id").build()
                    )
                val emailGateway = EmailGatewayImpl(sesAsyncClient, sesProperties)

                emailGateway.sendEmail(recipient, "[그라밋] 학교 메일 인증 코드가 도착했어요.", "<p>123456</p>", isHtml = true)

                val request = requestSlot.captured
                request.source() shouldBe "no-reply@gradmeet.co.kr"
                request.destination().toAddresses() shouldBe listOf(recipient)
                request.message().subject().data() shouldBe "[그라밋] 학교 메일 인증 코드가 도착했어요."
                request.message().body().html().data() shouldBe "<p>123456</p>"
                request.message().body().text().shouldBeNull()
            }
        }

        `when`("텍스트 메일을 발송하면") {
            then("본문이 text 로 구성된다") {
                val sesAsyncClient = mockk<SesAsyncClient>()
                val requestSlot = slot<SendEmailRequest>()
                every { sesAsyncClient.sendEmail(capture(requestSlot)) } returns
                    CompletableFuture.completedFuture(
                        SendEmailResponse.builder().messageId("test-message-id").build()
                    )
                val emailGateway = EmailGatewayImpl(sesAsyncClient, sesProperties)

                emailGateway.sendEmail(recipient, "제목", "본문", isHtml = false)

                val request = requestSlot.captured
                request.message().body().text().data() shouldBe "본문"
                request.message().body().html().shouldBeNull()
            }
        }
    }

    given("SES 가 오류를 반환하는 경우") {
        `when`("이메일 발송을 요청하면") {
            then("EmailSendFailedException 이 호출자에게 전파된다") {
                val sesAsyncClient = mockk<SesAsyncClient>()
                val sesException = SesException.builder()
                    .awsErrorDetails(
                        AwsErrorDetails.builder()
                            .errorCode("MessageRejected")
                            .errorMessage("Email address is not verified.")
                            .build()
                    )
                    .message("Email address is not verified.")
                    .build()
                every { sesAsyncClient.sendEmail(any<SendEmailRequest>()) } returns
                    CompletableFuture.failedFuture(sesException)
                val emailGateway = EmailGatewayImpl(sesAsyncClient, sesProperties)

                val exception = shouldThrow<EmailSendFailedException> {
                    emailGateway.sendEmail(recipient, "제목", "본문", isHtml = true)
                }

                exception.code shouldBe "EM0001"
                exception.cause shouldBe sesException
            }

            then("예외 메시지에 SES 오류 코드와 수신 도메인이 담긴다") {
                val sesAsyncClient = mockk<SesAsyncClient>()
                every { sesAsyncClient.sendEmail(any<SendEmailRequest>()) } returns
                    CompletableFuture.failedFuture(
                        SesException.builder()
                            .awsErrorDetails(
                                AwsErrorDetails.builder()
                                    .errorCode("Throttling")
                                    .errorMessage("Maximum sending rate exceeded.")
                                    .build()
                            )
                            .message("Maximum sending rate exceeded.")
                            .build()
                    )
                val emailGateway = EmailGatewayImpl(sesAsyncClient, sesProperties)

                val exception = shouldThrow<EmailSendFailedException> {
                    emailGateway.sendEmail(recipient, "제목", "본문", isHtml = true)
                }

                exception.message shouldContain "Throttling"
                exception.message shouldContain "Maximum sending rate exceeded."
                exception.message shouldContain "gsu.edu"
            }

            then("예외 메시지에 수신자 로컬 파트는 마스킹되어 담긴다") {
                val sesAsyncClient = mockk<SesAsyncClient>()
                every { sesAsyncClient.sendEmail(any<SendEmailRequest>()) } returns
                    CompletableFuture.failedFuture(RuntimeException("connection reset"))
                val emailGateway = EmailGatewayImpl(sesAsyncClient, sesProperties)

                val exception = shouldThrow<EmailSendFailedException> {
                    emailGateway.sendEmail(recipient, "제목", "본문", isHtml = true)
                }

                exception.message shouldContain "s*****@gsu.edu"
                exception.message shouldNotContain "ssim6@"
            }
        }
    }

    given("SES 호출이 SES 예외가 아닌 오류로 실패하는 경우") {
        `when`("이메일 발송을 요청하면") {
            then("원인 예외를 담은 EmailSendFailedException 이 전파된다") {
                val sesAsyncClient = mockk<SesAsyncClient>()
                val cause = RuntimeException("connection reset")
                every { sesAsyncClient.sendEmail(any<SendEmailRequest>()) } returns
                    CompletableFuture.failedFuture(cause)
                val emailGateway = EmailGatewayImpl(sesAsyncClient, sesProperties)

                val exception = shouldThrow<EmailSendFailedException> {
                    emailGateway.sendEmail(recipient, "제목", "본문", isHtml = true)
                }

                exception.cause shouldBe cause
                exception.message shouldContain "connection reset"
            }
        }
    }

    given("발송 실패가 계속되는 경우") {
        `when`("호출 측이 RetryUtils 로 감싸 발송하면") {
            then("설정된 횟수만큼 재시도한 뒤 예외가 전파된다") {
                val sesAsyncClient = mockk<SesAsyncClient>()
                every { sesAsyncClient.sendEmail(any<SendEmailRequest>()) } returns
                    CompletableFuture.failedFuture(RuntimeException("connection reset"))
                val emailGateway = EmailGatewayImpl(sesAsyncClient, sesProperties)

                shouldThrow<EmailSendFailedException> {
                    RetryUtils.retryWithBackOff(maxRetries = 3, defaultDelay = 1L, maxJitter = 1L) {
                        emailGateway.sendEmail(recipient, "제목", "본문", isHtml = true)
                    }
                }

                verify(exactly = 3) { sesAsyncClient.sendEmail(any<SendEmailRequest>()) }
            }
        }
    }
})
