package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.exception.MemberNotFoundException
import com.dobby.gateway.email.VerificationGateway
import com.dobby.gateway.member.MemberWithdrawalGateway
import com.dobby.gateway.member.ResearcherGateway
import com.dobby.model.member.MemberWithdrawal
import com.dobby.enums.member.WithdrawalReasonType

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
