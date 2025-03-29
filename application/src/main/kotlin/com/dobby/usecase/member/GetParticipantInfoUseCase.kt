package com.dobby.usecase.member

import com.dobby.enums.MatchType
import com.dobby.enums.member.GenderType
import com.dobby.exception.MemberConsentNotFoundException
import com.dobby.exception.ParticipantNotFoundException
import com.dobby.gateway.member.MemberConsentGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.gateway.member.ParticipantGateway
import com.dobby.model.member.Member
import com.dobby.model.member.Participant
import com.dobby.usecase.UseCase
import java.time.LocalDate

class GetParticipantInfoUseCase(
    private val memberGateway: MemberGateway,
    private val participantGateway: ParticipantGateway,
    private val memberConsentGateway: MemberConsentGateway
) : UseCase<GetParticipantInfoUseCase.Input, GetParticipantInfoUseCase.Output> {
    data class Input(
        val memberId: String
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
