package com.dobby.backend.application.mapper

import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.member.ParticipantEntity
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.infrastructure.database.entity.member.AddressInfo
import com.dobby.backend.presentation.api.dto.request.signup.AddressInfo as DtoAddressInfo

object SignupMapper {
    fun toAddressInfo(dto: DtoAddressInfo): AddressInfo {
        return AddressInfo(
            dto.region,
            dto.area
        )
    }
    fun toMember(req: ParticipantSignupRequest): MemberEntity {
        return MemberEntity(
            id = 0, // Auto-generated
            oauthEmail = req.oauthEmail,
            provider = req.provider,
            status = MemberStatus.ACTIVE,
            role = RoleType.PARTICIPANT,
            contactEmail = req.contactEmail,
            name = req.name,
            birthDate = req.birthDate
        )
    }
    fun toParticipant(
        member: MemberEntity,
        req: ParticipantSignupRequest
    ): ParticipantEntity {
        return ParticipantEntity(
            member = member,
            basicAddressInfo = toAddressInfo(req.basicAddressInfo),
            additionalAddressInfo = req.additionalAddressInfo?.let { toAddressInfo(it) },
            preferType = req.preferType,
            gender = req.gender
        )
    }
}
