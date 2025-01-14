package com.dobby.backend.infrastructure.converter

import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.member.ParticipantEntity
import com.dobby.backend.infrastructure.database.entity.member.AddressInfo as EntityAddressInfo

object ParticipantConverter {
    fun toModel(entity: ParticipantEntity): Participant {
        return Participant(
            member = Member(
                id = entity.member.id,
                contactEmail = entity.member.contactEmail,
                oauthEmail = entity.member.oauthEmail,
                provider = entity.member.provider,
                role = entity.member.role,
                name = entity.member.name,
                status = entity.member.status,
                createdAt = entity.member.createdAt,
                updatedAt = entity.member.updatedAt
            ),
            id = entity.id,
            gender = entity.gender,
            birthDate = entity.birthDate,
            basicAddressInfo = entity.basicAddressInfo.toModel(),
            additionalAddressInfo = entity.additionalAddressInfo?.toModel(),
            preferType = entity.preferType
        )
    }

    fun toEntity(participant: Participant): ParticipantEntity {
        val memberEntity = MemberEntity(
            id = participant.member.id,
            oauthEmail = participant.member.oauthEmail,
            provider = participant.member.provider,
            role = participant.member.role,
            contactEmail = participant.member.contactEmail,
            name = participant.member.name,
            createdAt = participant.member.createdAt,
            updatedAt = participant.member.updatedAt,
        )
        return ParticipantEntity(
            id = participant.id,
            member = memberEntity,
            gender = participant.gender,
            birthDate = participant.birthDate,
            preferType = participant.preferType,
            basicAddressInfo = participant.basicAddressInfo.toEntity(),
            additionalAddressInfo = participant.additionalAddressInfo?.toEntity()
        )
    }

    private fun EntityAddressInfo.toModel(): Participant.AddressInfo {
        return Participant.AddressInfo(
            region = this.region,
            area = this.area
        )
    }

    private fun Participant.AddressInfo.toEntity(): EntityAddressInfo {
        return EntityAddressInfo(
            region = this.region,
            area = this.area
        )
    }
}
