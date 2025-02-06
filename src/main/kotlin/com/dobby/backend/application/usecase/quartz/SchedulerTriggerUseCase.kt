package com.dobby.backend.application.usecase.quartz

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.SchedulerTriggerGateway

class SchedulerTriggerUseCase(
    private val schedulerTriggerGateway: SchedulerTriggerGateway
): UseCase<SchedulerTriggerUseCase.Input, SchedulerTriggerUseCase.Output> {

    data class Input(
        val jobName: String,
        val jobGroup: String = "DEFAULT"
    )

    object Output

    override fun execute(input: Input): Output {
        schedulerTriggerGateway.triggerJob(input.jobName, input.jobGroup)
        return Output
    }

}
