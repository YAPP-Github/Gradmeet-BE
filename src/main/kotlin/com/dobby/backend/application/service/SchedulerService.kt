package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.quartz.SchedulerTriggerUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val schedulerTriggerUseCase: SchedulerTriggerUseCase
) {
    @Transactional
    fun triggerSendMatchingEmailJob() {
        schedulerTriggerUseCase.execute(SchedulerTriggerUseCase.Input("matching_email_send_job"))
    }
}
