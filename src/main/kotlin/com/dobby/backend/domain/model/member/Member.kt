package com.dobby.backend.domain.model.member

import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType

data class Member(
    val id: Long,
    val name: String?,
    val oauthEmail: String,
    val contactEmail: String?,
    val provider: ProviderType,
    val status: MemberStatus,
    val role: RoleType?,
) {

    companion object {
        fun newMember(
            id: Long,
            name: String,
            oauthEmail: String,
            contactEmail: String,
            provider: ProviderType,
            status: MemberStatus,
            role: RoleType,
        ) = Member(
            id = id,
            name = name,
            oauthEmail = oauthEmail,
            contactEmail = contactEmail,
            provider = provider,
            status = status,
            role = role
        )
    }
}
