package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.quartz.TriggerSchedulerUseCase
import com.dobby.backend.domain.gateway.member.MemberConsentGateway
import com.dobby.backend.domain.gateway.member.MemberGateway
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val schedulerTriggerUseCase: TriggerSchedulerUseCase,
    private val memberConsentGateway: MemberConsentGateway
) {
    @Transactional
    fun triggerSendMatchingEmailJob() {
        schedulerTriggerUseCase.execute(TriggerSchedulerUseCase.Input("matching_email_send_job"))
    }

    fun updateMemberConsent(memberId: String) {
        memberConsentGateway.updateMatchConsent(memberId, false)
    }
}
