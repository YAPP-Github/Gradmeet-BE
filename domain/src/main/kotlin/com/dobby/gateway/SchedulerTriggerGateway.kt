package com.dobby.gateway

interface SchedulerTriggerGateway {
    fun triggerJob(jobName: String, jobGroup: String)
}
