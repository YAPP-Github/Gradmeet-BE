package com.dobby.backend.domain.model.member

import com.dobby.backend.infrastructure.database.entity.enums.member.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enums.member.ProviderType
import com.dobby.backend.infrastructure.database.entity.enums.member.RoleType
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
        name = "ExMember",
        oauthEmail = "Deleted_${id}",
        contactEmail = "Deleted_${id}",
        status = MemberStatus.HOLD,
        updatedAt = LocalDateTime.now(),
        deletedAt = LocalDateTime.now(),
    )
}
