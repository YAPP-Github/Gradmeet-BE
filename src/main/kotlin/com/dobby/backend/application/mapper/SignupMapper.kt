package com.dobby.backend.application.mapper
import com.dobby.backend.application.usecase.member.CreateResearcherUseCase
import com.dobby.backend.application.usecase.member.CreateParticipantUseCase
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.domain.model.member.Researcher

object SignupMapper {
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
