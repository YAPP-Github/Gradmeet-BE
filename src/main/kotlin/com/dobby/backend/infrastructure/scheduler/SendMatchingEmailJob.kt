package com.dobby.backend.infrastructure.scheduler

import com.dobby.backend.application.service.EmailService
import com.dobby.backend.application.usecase.member.email.SendMatcingEmailUseCase
import com.dobby.backend.application.usecase.member.email.GetMatchingExperimentPostsUseCase
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SendMatchingEmailJob(
    private val emailService: EmailService
): Job {
    companion object {
        private val logger : Logger = LoggerFactory.getLogger(SendMatchingEmailJob::class.java)
    }

    override fun execute(context: JobExecutionContext) {
        logger.info("🔔 BulkMatchingEmailSendJob started at ${LocalDateTime.now()}")

        val input = GetMatchingExperimentPostsUseCase.Input(
            requestTime = LocalDateTime.now()
        )
        val output = emailService.getMatchingInfo(input)
        val matchingExperimentPosts = output.matchingPosts

        var successCount = 0
        var failureCount = 0

        for((contactEmail, jobList) in matchingExperimentPosts) {
            if(jobList.isEmpty()) continue
            val emailInput = SendMatcingEmailUseCase.Input (
                contactEmail = contactEmail,
                experimentPosts = jobList,
                currentDateTime = LocalDateTime.now()
            )
            try {
                val emailOutput = emailService.sendMatchingEmail(emailInput)
                if (emailOutput.isSuccess) {
                    successCount++
                    logger.info("✅ Email sent successfully to $contactEmail")
                } else {
                    failureCount++
                    logger.error("❌ Failed to send email to $contactEmail: ${emailOutput.message}")
                }
            } catch (e: Exception) {
                failureCount++
                logger.error("❌ Exception occurred while sending email to $contactEmail", e)
            }
        }
        logger.info("🎯 MatchingEmailSendJob completed. Success: $successCount, Failures: $failureCount")
    }
}
