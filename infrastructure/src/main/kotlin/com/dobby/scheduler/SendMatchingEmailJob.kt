package com.dobby.scheduler

import com.dobby.service.EmailService
import com.dobby.usecase.member.email.SendMatchingEmailUseCase
import com.dobby.usecase.member.email.GetMatchingExperimentPostsUseCase
import com.dobby.util.TimeProvider
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
        private val logger: Logger = LoggerFactory.getLogger(SendMatchingEmailJob::class.java)
    }

    override fun execute(context: JobExecutionContext) {
        logger.info("BulkSendMatchingEmailJob started at ${TimeProvider.currentDateTime()}")

        val input = GetMatchingExperimentPostsUseCase.Input(
            requestTime = TimeProvider.currentDateTime()
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
            try {
                val emailOutput = emailService.sendMatchingEmail(emailInput)
                if (emailOutput.isSuccess) {
                    successCount++
                    logger.info("Email sent successfully to $contactEmail")
                } else {
                    failureCount++
                    logger.error("Failed to send email to $contactEmail: ${emailOutput.message}")
                }
            } catch (e: Exception) {
                failureCount++
                logger.error("Exception occurred while sending email to $contactEmail", e)
            }
        }
        logger.info("SendMatchingEmailJob completed. Success: $successCount, Failures: $failureCount")
    }
}
