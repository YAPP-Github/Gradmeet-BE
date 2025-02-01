package com.dobby.backend.domain.model.member

import com.dobby.backend.infrastructure.database.entity.enums.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enums.ProviderType
import com.dobby.backend.infrastructure.database.entity.enums.RoleType
import com.dobby.backend.util.generateTSID
import java.time.LocalDateTime

data class Member(
    val id: Long,
    val name: String,
    val oauthEmail: String,
    val contactEmail: String?,
    val provider: ProviderType,
    var status: MemberStatus,
    val role: RoleType?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {

    companion object {
        fun newMember(
            name: String,
            oauthEmail: String,
            contactEmail: String,
            provider: ProviderType,
            role: RoleType,
        ) = Member(
            id = generateTSID(),
            name = name,
            oauthEmail = oauthEmail,
            contactEmail = contactEmail,
            provider = provider,
            status = MemberStatus.ACTIVE,
            role = role,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}
