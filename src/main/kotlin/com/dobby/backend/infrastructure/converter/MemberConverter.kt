package com.dobby.backend.infrastructure.converter

import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity

object MemberConverter {
    fun toEntity(model: Member): MemberEntity{
        return MemberEntity(
            id = model.id,
            name = model.name,
            contactEmail = model.contactEmail,
            oauthEmail = model.oauthEmail,
            role = model.role,
            provider = model.provider,
            status = model.status,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )
    }

    fun toModel(entity: MemberEntity): Member{
        return Member(
            id = entity.id,
            name = entity.name,
            contactEmail = entity.contactEmail,
            oauthEmail = entity.oauthEmail,
            role = entity.role,
            provider = entity.provider,
            status = entity.status,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}
