package com.dobby.backend.domain.gateway

interface SchedulerTriggerGateway {
    fun triggerJob(jobName: String, jobGroup: String)
}
