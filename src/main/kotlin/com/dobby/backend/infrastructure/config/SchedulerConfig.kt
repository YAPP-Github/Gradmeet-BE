package com.dobby.backend.infrastructure.config

import com.dobby.backend.infrastructure.scheduler.ExpiredExperimentPostJob
import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class SchedulerConfig {
    @Bean
    fun expiredExperimentPostJobDetail(): JobDetail {
        return JobBuilder.newJob(ExpiredExperimentPostJob::class.java)
            .withIdentity("expired_experiment_post_job")
            .storeDurably()
            .build()
    }

    @Bean
    fun expiredExperimentPostTrigger(): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(expiredExperimentPostJobDetail())
            .withIdentity("expired_experiment_post_trigger")
            .withSchedule(
                CronScheduleBuilder.dailyAtHourAndMinute(0, 0)
                    .inTimeZone(TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }
}
