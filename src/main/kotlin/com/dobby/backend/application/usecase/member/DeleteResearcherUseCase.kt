package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.MemberNotFoundException
import com.dobby.backend.domain.gateway.email.VerificationGateway
import com.dobby.backend.domain.gateway.member.MemberWithdrawalGateway
import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.MemberWithdrawal
import com.dobby.backend.domain.enums.member.WithdrawalReasonType

class DeleteResearcherUseCase(
    private val researcherGateway: ResearcherGateway,
    private val memberWithdrawalGateway: MemberWithdrawalGateway,
    private val verificationGateway: VerificationGateway
) : UseCase<DeleteResearcherUseCase.Input, DeleteResearcherUseCase.Output> {

    data class Input(
        val memberId: String,
        val reasonType: WithdrawalReasonType,
        val reason: String?
    )

    data class Output(
        val isSuccess: Boolean
    )

    override fun execute(input: Input): Output {
        val researcher = researcherGateway.findByMemberId(input.memberId)
            ?: throw MemberNotFoundException

        verificationGateway.deleteByUnivEmail(researcher.univEmail)
        researcherGateway.save(researcher.withdraw())

        memberWithdrawalGateway.save(
            MemberWithdrawal.newWithdrawal(
                memberId = input.memberId,
                reasonType = input.reasonType,
                otherReason = input.reason
            )
        )

        return Output(isSuccess = true)
    }
}
