package com.dobby.backend.infrastructure.converter

import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.member.ResearcherEntity

object ResearcherConverter {
    fun toModel(entity: ResearcherEntity): Researcher{
        return Researcher(
            member = Member(
                id = entity.member.id,
                contactEmail = entity.member.contactEmail,
                oauthEmail = entity.member.oauthEmail,
                provider = entity.member.provider,
                role = RoleType.RESEARCHER,
                name = entity.member.name,
                status = entity.member.status,
                createdAt = entity.member.createdAt,
                updatedAt = entity.member.updatedAt
            ),
            id = entity.id,
            univEmail = entity.univEmail,
            univName = entity.univName,
            emailVerified = entity.emailVerified,
            major = entity.major,
            labInfo = entity.labInfo
        )
    }

    fun toEntity(researcher: Researcher): ResearcherEntity {
        val memberEntity = MemberEntity(
            id = researcher.member.id,
            oauthEmail = researcher.member.oauthEmail,
            provider = researcher.member.provider,
            role = researcher.member.role,
            contactEmail = researcher.member.contactEmail,
            name = researcher.member.name,
            createdAt = researcher.member.createdAt,
            updatedAt = researcher.member.updatedAt,
        )

        return ResearcherEntity(
            id = researcher.id,
            member = memberEntity,
            univEmail = researcher.univEmail,
            emailVerified = researcher.emailVerified,
            univName = researcher.univName,
            major = researcher.major,
            labInfo = researcher.labInfo
        )
    }}
