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
                memberId = entity.member.id,
                contactEmail = entity.member.contactEmail,
                oauthEmail = entity.member.oauthEmail,
                provider = entity.member.provider,
                role = RoleType.RESEARCHER,
                name = entity.name,
                status = entity.status
            ),
            univEmail = entity.univEmail,
            univName = entity.univName,
            emailVerified = entity.emailVerified,
            major = entity.major,
            labInfo = entity.labInfo
        )
    }

    fun toEntity(researcher: Researcher): ResearcherEntity {
        val memberEntity = MemberEntity(
            id = researcher.member.memberId,
            oauthEmail = researcher.member.oauthEmail,
            provider = researcher.member.provider,
            role = researcher.member.role,
            contactEmail = researcher.member.contactEmail,
            name = researcher.member.name
        )

        return ResearcherEntity(
            member = memberEntity,
            univEmail = researcher.univEmail,
            emailVerified = researcher.emailVerified,
            univName = researcher.univName,
            major = researcher.major,
            labInfo = researcher.labInfo
        )
    }}
