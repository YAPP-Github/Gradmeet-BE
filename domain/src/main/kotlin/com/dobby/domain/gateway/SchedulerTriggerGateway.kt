package com.dobby.domain.gateway

interface SchedulerTriggerGateway {
    fun triggerJob(jobName: String, jobGroup: String)
}
