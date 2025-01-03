package com.dobby.backend.domain.model

import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import java.time.LocalDate

data class Member(
    val memberId: Long,
    val name: String?,
    val oauthEmail: String,
    val contactEmail: String?,
    val provider: ProviderType,
    val status: MemberStatus,
    val role: RoleType?,
    val birthDate: LocalDate?
) {

    companion object {
        fun newMember(
            memberId: Long,
            name: String,
            oauthEmail: String,
            contactEmail: String,
            provider: ProviderType,
            status: MemberStatus,
            role: RoleType,
            birthDate: LocalDate,
        ) = Member(
            memberId = memberId,
            name = name,
            oauthEmail = oauthEmail,
            contactEmail = contactEmail,
            provider = provider,
            status = status,
            role = role,
            birthDate = birthDate,
        )
    }
}
