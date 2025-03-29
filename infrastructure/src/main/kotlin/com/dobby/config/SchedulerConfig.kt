package com.dobby.config

import com.dobby.scheduler.ExpiredExperimentPostJob
import com.dobby.scheduler.SendMatchingEmailJob
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.TriggerBuilder
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
    fun sendEmailMatchingJobDetail(): JobDetail {
        logger.info("Registering SendEmailMatchingJobDetail...")
        return JobBuilder.newJob(SendMatchingEmailJob::class.java)
            .withIdentity("matching_email_send_job", "DEFAULT")
            .storeDurably()
            .build()
    }

    @Bean
    fun sendEmailMatchingPostTrigger(): Trigger {
        logger.info("Registering SendEmailMatchingPostTrigger...")
        return TriggerBuilder.newTrigger()
            .forJob(sendEmailMatchingJobDetail())
            .withIdentity("send_matching_email_trigger", "DEFAULT")
            .startNow()
            .withSchedule(
                CronScheduleBuilder.dailyAtHourAndMinute(8, 0)
                    .inTimeZone(TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }
}
