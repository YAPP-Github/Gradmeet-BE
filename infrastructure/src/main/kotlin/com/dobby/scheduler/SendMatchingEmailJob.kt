package com.dobby.scheduler

import com.dobby.gateway.AlertGateway
import com.dobby.service.EmailService
import com.dobby.usecase.member.email.GetMatchingExperimentPostsUseCase
import com.dobby.usecase.member.email.SendMatchingEmailUseCase
import com.dobby.util.TimeProvider
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate

@Component
class SendMatchingEmailJob(
    private val emailService: EmailService,
    private val alertGateway: AlertGateway
) : Job {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SendMatchingEmailJob::class.java)
    }

    override fun execute(context: JobExecutionContext) {
        val start = TimeProvider.currentDateTime()
        logger.info("BulkSendMatchingEmailJob started at $start")

        val input = GetMatchingExperimentPostsUseCase.Input(
            requestTime = start
        )
        val output = emailService.getMatchingInfo(input)
        val matchingExperimentPosts = output.matchingPosts

        var successCount = 0
        var failureCount = 0

        for ((contactEmail, jobList) in matchingExperimentPosts) {
            if (jobList.isEmpty()) continue

            val emailInput = SendMatchingEmailUseCase.Input(
                contactEmail = contactEmail,
                experimentPosts = jobList,
                currentDateTime = TimeProvider.currentDateTime()
            )

            runCatching {
                emailService.sendMatchingEmail(emailInput)
            }.onSuccess { result ->
                if (result.isSuccess) {
                    successCount++
                    logger.info("Email sent successfully to $contactEmail")
                } else {
                    failureCount++
                    logger.error("Failed to send email to $contactEmail: ${result.message}")
                }
            }.onFailure { e ->
                failureCount++
                logger.error("Exception while sending email to $contactEmail", e)
            }
        }

        val end = TimeProvider.currentDateTime()
        val took = Duration.between(start, end).toSeconds()

        val windowStart = MDC.get("match.windowStart") ?: start.toString()
        val windowEnd = MDC.get("match.windowEnd") ?: end.toString()
        val todayCount = MDC.get("match.todayPosts")?.toIntOrNull() ?: 0
        val consentParticipants = MDC.get("match.consentParticipants")?.toIntOrNull() ?: 0
        val matchedRecipients = MDC.get("match.matchedRecipients")?.toIntOrNull() ?: matchingExperimentPosts.size

        listOf(
            "match.windowStart",
            "match.windowEnd",
            "match.todayPosts",
            "match.consentParticipants",
            "match.matchedRecipients"
        ).forEach { MDC.remove(it) }

        logger.info("SendMatchingEmailJob completed. Success=$successCount, Failures=$failureCount, Took=${took}s")

        val today = LocalDate.now()

        alertGateway.sendNotify(
            title = "# 📤 $today 매칭 메일 발송 결과",
            description = """
            **집계 구간**: $windowStart ~ $windowEnd (KST)
            🗓️ **오늘 올라온 공고 수**: **$todayCount** 건
            👤 **알림 동의한 전체 수**: **$consentParticipants** 명
            ✉️ **매칭 발송된 대상자(이메일)**: **$matchedRecipients** 명

            ---
            ✅ **발송 성공**: **$successCount** 건
            ❌ **발송 실패**: **$failureCount** 건
            ⏰ **실행 시간**: $took 초
            🕒 **완료 시각**: $end
            """.trimIndent(),
            content = "@here"
        )
    }
}
