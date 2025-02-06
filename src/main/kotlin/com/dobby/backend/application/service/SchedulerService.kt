package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.quartz.TriggerSchedulerUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val schedulerTriggerUseCase: TriggerSchedulerUseCase
) {
    @Transactional
    fun triggerSendMatchingEmailJob() {
        schedulerTriggerUseCase.execute(TriggerSchedulerUseCase.Input("matching_email_send_job"))
    }
}
