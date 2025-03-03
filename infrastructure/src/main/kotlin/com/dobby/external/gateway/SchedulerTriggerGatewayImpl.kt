package com.dobby.external.gateway

import org.quartz.Scheduler
import org.quartz.JobKey
import org.springframework.stereotype.Component

@Component
class SchedulerTriggerGatewayImpl(
    private val scheduler: Scheduler
) : SchedulerTriggerGateway {
    override fun triggerJob(jobName: String, jobGroup: String) {
        val jobKey = JobKey.jobKey(jobName, jobGroup)
        if (scheduler.checkExists(jobKey)) {
            scheduler.triggerJob(jobKey)
        } else {
            throw IllegalArgumentException("Job with name $jobName and group $jobGroup does not exist.")
        }
    }
}

