package com.dobby.backend.application.mapper
import com.dobby.backend.application.usecase.member.CreateResearcherUseCase
import com.dobby.backend.application.usecase.member.CreateParticipantUseCase
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.member.ParticipantEntity
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.infrastructure.database.entity.member.AddressInfo
import com.dobby.backend.infrastructure.database.entity.member.ResearcherEntity
import com.dobby.backend.presentation.api.dto.request.signup.AddressInfo as DtoAddressInfo

object SignupMapper {
    fun toAddressInfo(dto: DtoAddressInfo): AddressInfo {
        return AddressInfo(
            dto.region,
            dto.area
        )
    }

    fun toParticipant(
        member: MemberEntity,
        req: ParticipantSignupRequest
    ): ParticipantEntity {
        return ParticipantEntity(
            id = 0,
            member = member,
            basicAddressInfo = toAddressInfo(req.basicAddressInfo),
            additionalAddressInfo = req.additionalAddressInfo?.let { toAddressInfo(it) },
            matchType = req.matchType,
            gender = req.gender,
            birthDate = req.birthDate
        )
    }

    fun toResearcher(
        member: MemberEntity,
        req: CreateResearcherUseCase.Input
    ): ResearcherEntity {
        return ResearcherEntity(
            id = 0,
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
            memberId = newResearcher.member.id,
            name = newResearcher.member.name,
            oauthEmail = newResearcher.member.oauthEmail,
            provider = newResearcher.member.provider,
            role = newResearcher.member.role
        )
    }

    fun modelToParticipantRes(newParticipant: Participant)
    : CreateParticipantUseCase.MemberResponse {
        return CreateParticipantUseCase.MemberResponse(
            memberId = newParticipant.member.id,
            name = newParticipant.member.name,
            oauthEmail = newParticipant.member.oauthEmail,
            provider = newParticipant.member.provider,
            role = newParticipant.member.role
        )
    }
}
