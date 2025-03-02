package com.dobby.domain.model.member

import com.dobby.domain.policy.MemberMaskingPolicy
import com.dobby.domain.enums.member.MemberStatus
import com.dobby.domain.enums.member.ProviderType
import com.dobby.domain.enums.member.RoleType
import java.time.LocalDateTime

data class Member(
    val id: String,
    val name: String,
    val oauthEmail: String,
    val contactEmail: String?,
    val provider: ProviderType,
    var status: MemberStatus,
    val role: RoleType?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
) {

    companion object {
        fun newMember(
            id: String,
            name: String,
            oauthEmail: String,
            contactEmail: String,
            provider: ProviderType,
            role: RoleType,
        ) = Member(
            id = id,
            name = name,
            oauthEmail = oauthEmail,
            contactEmail = contactEmail,
            provider = provider,
            status = MemberStatus.ACTIVE,
            role = role,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    fun withdraw(): Member = copy(
        name = MemberMaskingPolicy.maskName(),
        oauthEmail = MemberMaskingPolicy.maskSensitiveData(this.id),
        contactEmail = MemberMaskingPolicy.maskSensitiveData(this.id),
        status = MemberStatus.HOLD,
        updatedAt = LocalDateTime.now(),
        deletedAt = LocalDateTime.now(),
    )
}
