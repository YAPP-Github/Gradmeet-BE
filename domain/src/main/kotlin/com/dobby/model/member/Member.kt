package com.dobby.model.member

import com.dobby.policy.MemberMaskingPolicy
import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.ProviderType
import com.dobby.enums.member.RoleType
import com.dobby.util.TimeProvider
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
            createdAt = TimeProvider.currentDateTime(),
            updatedAt = TimeProvider.currentDateTime(),
            deletedAt = null
        )
    }

    fun withdraw(): Member = copy(
        name = MemberMaskingPolicy.maskName(),
        oauthEmail = MemberMaskingPolicy.maskSensitiveData(this.id),
        contactEmail = MemberMaskingPolicy.maskSensitiveData(this.id),
        status = MemberStatus.HOLD,
        updatedAt = TimeProvider.currentDateTime(),
        deletedAt = TimeProvider.currentDateTime(),
    )
}
