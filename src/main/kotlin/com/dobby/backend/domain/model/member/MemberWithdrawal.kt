package com.dobby.backend.domain.model.member

import com.dobby.backend.infrastructure.database.entity.enums.member.WithdrawalReasonType
import java.time.LocalDateTime

data class MemberWithdrawal(
    val memberId: String,
    val reasonType: WithdrawalReasonType,
    val otherReason: String? = null,
    val createdAt: LocalDateTime
) {
    companion object {
        fun newWithdrawal(
            memberId: String,
            reasonType: WithdrawalReasonType,
            otherReason: String? = null,
            createdAt: LocalDateTime = LocalDateTime.now()
        ) = MemberWithdrawal(
            memberId,
            reasonType,
            otherReason,
            createdAt
        )
    }
}
