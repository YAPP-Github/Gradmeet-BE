package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.quartz.TriggerSchedulerUseCase
import com.dobby.domain.exception.MemberNotFoundException
import com.dobby.domain.gateway.member.MemberConsentGateway
import com.dobby.domain.gateway.member.MemberGateway
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val schedulerTriggerUseCase: TriggerSchedulerUseCase,
    private val memberConsentGateway: MemberConsentGateway,
    private val memberGateway: MemberGateway
) {
    @Transactional
    fun triggerSendMatchingEmailJob() {
        schedulerTriggerUseCase.execute(TriggerSchedulerUseCase.Input("matching_email_send_job"))
    }

    @Transactional
    fun updateMemberConsent(name: String) {
        val memberId = memberGateway.findMemberIdByName(name)
            ?: throw MemberNotFoundException
        memberConsentGateway.updateMatchConsent(memberId, false)
    }
}
