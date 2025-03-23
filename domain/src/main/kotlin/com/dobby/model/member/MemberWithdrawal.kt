package com.dobby.model.member

import com.dobby.enums.member.WithdrawalReasonType
import com.dobby.util.TimeProvider
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
            createdAt: LocalDateTime = TimeProvider.currentDateTime()
        ) = MemberWithdrawal(
            memberId,
            reasonType,
            otherReason,
            createdAt
        )
    }
}
