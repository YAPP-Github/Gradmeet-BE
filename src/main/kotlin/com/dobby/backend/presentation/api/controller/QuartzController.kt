package com.dobby.backend.presentation.api.controller
import com.dobby.backend.infrastructure.scheduler.SendMatchingEmailJob
import org.quartz.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test/quartz")
class QuartzController(private val scheduler: Scheduler) {

    @GetMapping("/run-job")
    fun runMatchingEmailSendJob() {
        val jobDetail = JobBuilder.newJob(SendMatchingEmailJob::class.java)
            .withIdentity("manual_matching_email_send_job")
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .startNow()
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }
}
