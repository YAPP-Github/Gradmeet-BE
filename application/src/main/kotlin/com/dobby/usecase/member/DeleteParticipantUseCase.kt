package com.dobby.usecase.member

import com.dobby.enums.member.WithdrawalReasonType
import com.dobby.exception.MemberNotFoundException
import com.dobby.gateway.member.MemberGateway
import com.dobby.gateway.member.MemberWithdrawalGateway
import com.dobby.model.member.MemberWithdrawal
import com.dobby.usecase.UseCase

class DeleteParticipantUseCase(
    private val memberGateway: MemberGateway,
    private val memberWithdrawalGateway: MemberWithdrawalGateway
) : UseCase<DeleteParticipantUseCase.Input, DeleteParticipantUseCase.Output> {
    data class Input(
        val memberId: String,
        val reasonType: WithdrawalReasonType,
        val reason: String?
    )

    data class Output(
        val isSuccess: Boolean
    )

    override fun execute(input: Input): Output {
        val member = memberGateway.findByIdAndDeletedAtIsNull(input.memberId)
            ?: throw MemberNotFoundException

        memberGateway.save(member.withdraw())

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
