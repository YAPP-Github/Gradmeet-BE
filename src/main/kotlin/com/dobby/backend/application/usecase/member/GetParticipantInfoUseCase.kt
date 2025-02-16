package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.MemberConsentNotFoundException
import com.dobby.backend.domain.exception.ParticipantNotFoundException
import com.dobby.backend.domain.gateway.member.MemberConsentGateway
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.ParticipantGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import java.time.LocalDate

class GetParticipantInfoUseCase(
    private val memberGateway: MemberGateway,
    private val participantGateway: ParticipantGateway,
    private val memberConsentGateway: MemberConsentGateway
) : UseCase<GetParticipantInfoUseCase.Input, GetParticipantInfoUseCase.Output>{
    data class Input(
        val memberId: String,
    )

    data class Output(
        val member: Member,
        val gender: GenderType,
        val birthDate: LocalDate,
        val basicAddressInfo: Participant.AddressInfo,
        val additionalAddressInfo: Participant.AddressInfo?,
        val matchType: MatchType?,
        val adConsent: Boolean,
        val matchConsent: Boolean
    )

    override fun execute(input: Input): Output {
        val memberId = input.memberId
        val member = memberGateway.getById(memberId)
        val participant = participantGateway.findByMemberId(memberId)
            ?: throw ParticipantNotFoundException
        val participantConsent = memberConsentGateway.findByMemberId(memberId)
            ?: throw MemberConsentNotFoundException

        return Output(
            member = member,
            gender = participant.gender,
            birthDate = participant.birthDate,
            basicAddressInfo = participant.basicAddressInfo,
            additionalAddressInfo = participant.additionalAddressInfo,
            matchType = participant.matchType,
            adConsent = participantConsent.adConsent,
            matchConsent = participantConsent.matchConsent
        )
    }
}
