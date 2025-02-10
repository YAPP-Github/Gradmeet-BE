package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.MemberNotFoundException
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.MemberWithdrawalGateway
import com.dobby.backend.domain.model.member.MemberWithdrawal
import com.dobby.backend.infrastructure.database.entity.enums.member.WithdrawalReasonType

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
