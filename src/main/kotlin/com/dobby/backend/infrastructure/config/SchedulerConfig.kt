package com.dobby.backend.infrastructure.config

import com.dobby.backend.infrastructure.scheduler.ExpiredExperimentPostJob
import com.dobby.backend.infrastructure.scheduler.SendMatchingEmailJob
import org.quartz.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class SchedulerConfig {
    private val logger: Logger = LoggerFactory.getLogger(SchedulerConfig::class.java)

    @Bean
    fun expiredExperimentPostJobDetail(): JobDetail {
        logger.info("Registering ExpiredExperimentPostJob...")
        return JobBuilder.newJob(ExpiredExperimentPostJob::class.java)
            .withIdentity("expired_experiment_post_job", "DEFAULT")
            .storeDurably()
            .build()
    }

    @Bean
    fun expiredExperimentPostTrigger(): Trigger {
        logger.info("Registering ExpiredExperimentPostTrigger...")
        return TriggerBuilder.newTrigger()
            .forJob(expiredExperimentPostJobDetail())
            .withIdentity("expired_experiment_post_trigger", "DEFAULT")
            .withSchedule(
                CronScheduleBuilder.dailyAtHourAndMinute(0, 0)
                    .inTimeZone(TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }

    @Bean
    fun matchingEmailSendJobDetail(): JobDetail {
        logger.info("Registering MatchingEmailSendJob...")
        return JobBuilder.newJob(SendMatchingEmailJob::class.java)
            .withIdentity("matching_email_send_job", "DEFAULT")
            .storeDurably()
            .build()
    }

    @Bean
    fun matchingEmailSendTrigger() : Trigger {
        logger.info("Registering MatchingEmailSendTrigger...")
        return TriggerBuilder.newTrigger()
            .forJob(matchingEmailSendJobDetail())
            .withIdentity("matching_email_send_trigger", "DEFAULT")
            .startNow() // instantly execution for test
            .withSchedule(
                CronScheduleBuilder.dailyAtHourAndMinute(8,0)
                    .inTimeZone(TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }
}
