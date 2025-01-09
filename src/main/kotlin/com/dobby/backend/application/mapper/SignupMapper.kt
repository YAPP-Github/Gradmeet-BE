package com.dobby.backend.application.mapper
import com.dobby.backend.application.usecase.signupUseCase.CreateResearcherUseCase
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.member.ParticipantEntity
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.infrastructure.database.entity.member.AddressInfo
import com.dobby.backend.infrastructure.database.entity.member.ResearcherEntity
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.request.signup.AddressInfo as DtoAddressInfo

object SignupMapper {
    fun toAddressInfo(dto: DtoAddressInfo): AddressInfo {
        return AddressInfo(
            dto.region,
            dto.area
        )
    }
    fun toParticipantMember(req: ParticipantSignupRequest): MemberEntity {
        return MemberEntity(
            id = 0, // Auto-generated
            oauthEmail = req.oauthEmail,
            provider = req.provider,
            status = MemberStatus.ACTIVE,
            role = RoleType.PARTICIPANT,
            contactEmail = req.contactEmail,
            name = req.name
        )
    }

    fun toResearcherMember(req: CreateResearcherUseCase.Input): MemberEntity {
        return MemberEntity(
            id = 0, // Auto-generated
            oauthEmail = req.oauthEmail,
            provider = req.provider,
            status = MemberStatus.ACTIVE,
            role = RoleType.RESEARCHER,
            contactEmail = req.contactEmail,
            name = req.name,
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
            gender = req.gender,
            birthDate = req.birthDate
        )
    }

    fun toResearcher(
        member: MemberEntity,
        req: CreateResearcherUseCase.Input
    ): ResearcherEntity {
        return ResearcherEntity(
            member = member,
            univEmail = req.univEmail,
            emailVerified = req.emailVerified,
            univName = req.univName,
            major = req.major,
            labInfo = req.labInfo
        )
    }

    fun modelToResearcherRes(newResearcher: Researcher)
    : CreateResearcherUseCase.MemberResponse {
        return CreateResearcherUseCase.MemberResponse(
            memberId = newResearcher.member.memberId,
            name = newResearcher.member.name,
            oauthEmail = newResearcher.member.oauthEmail,
            provider = newResearcher.member.provider,
            role = newResearcher.member.role
        )
    }
}
