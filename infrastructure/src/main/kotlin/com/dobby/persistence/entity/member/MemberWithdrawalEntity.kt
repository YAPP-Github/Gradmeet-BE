package com.dobby.persistence.entity.member

import com.dobby.enums.member.WithdrawalReasonType
import com.dobby.model.member.MemberWithdrawal
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "member_withdrawal")
class MemberWithdrawalEntity(
    @Id
    @Column(name = "member_id", columnDefinition = "CHAR(13)")
    val memberId: String,

    @Column(name = "reason_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val reasonType: WithdrawalReasonType,

    @Column(name = "other_reason", nullable = true)
    val otherReason: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime
) {
    fun toDomain() = MemberWithdrawal(
        memberId = memberId,
        reasonType = reasonType,
        otherReason = otherReason,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(withdrawal: MemberWithdrawal) = with(withdrawal) {
            MemberWithdrawalEntity(
                memberId = memberId,
                reasonType = reasonType,
                otherReason = otherReason,
                createdAt = createdAt
            )
        }
    }
}
