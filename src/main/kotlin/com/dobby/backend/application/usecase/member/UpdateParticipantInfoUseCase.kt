package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.exception.ContactEmailDuplicateException
import com.dobby.exception.MemberConsentNotFoundException
import com.dobby.exception.ParticipantNotFoundException
import com.dobby.gateway.member.MemberConsentGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.gateway.member.ParticipantGateway
import com.dobby.model.member.Member
import com.dobby.model.member.Participant
import com.dobby.enums.member.GenderType
import com.dobby.enums.MatchType
import java.time.LocalDate

class UpdateParticipantInfoUseCase(
    private val participantGateway: ParticipantGateway,
    private val memberGateway: MemberGateway,
    private val memberConsentGateway: MemberConsentGateway
) : UseCase<UpdateParticipantInfoUseCase.Input, UpdateParticipantInfoUseCase.Output> {

    data class Input(
        val memberId: String,
        val contactEmail: String,
        val name: String,
        val basicAddressInfo: Participant.AddressInfo,
        val additionalAddressInfo: Participant.AddressInfo?,
        val matchType: MatchType?,
        val adConsent: Boolean,
        val matchConsent: Boolean
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
        val participant = participantGateway.findByMemberId(input.memberId)
            ?: throw ParticipantNotFoundException
        val participantConsent = memberConsentGateway.findByMemberId(input.memberId)
            ?: throw MemberConsentNotFoundException
        if (memberGateway.existsByContactEmail(input.contactEmail) && participant.member.contactEmail != input.contactEmail) {
            throw ContactEmailDuplicateException
        }

        val updatedParticipant = participantGateway.save(
            participant.updateInfo(
                contactEmail = input.contactEmail,
                name = input.name,
                basicAddressInfo = Participant.AddressInfo(
                    region = input.basicAddressInfo.region,
                    area = input.basicAddressInfo.area
                ),
                additionalAddressInfo = input.additionalAddressInfo?.let {
                    Participant.AddressInfo(region = it.region, area = it.area)
                },
                matchType = input.matchType
            )
        )

        val updatedConsent = memberConsentGateway.save(
            participantConsent.update(
                adConsent = input.adConsent,
                matchConsent = input.matchConsent
            )
        )


        return Output(
            member = updatedParticipant.member,
            gender = updatedParticipant.gender,
            birthDate = updatedParticipant.birthDate,
            basicAddressInfo = updatedParticipant.basicAddressInfo,
            additionalAddressInfo = updatedParticipant.additionalAddressInfo,
            matchType = updatedParticipant.matchType,
            adConsent = updatedConsent.adConsent,
            matchConsent = updatedConsent.matchConsent
        )
    }
}
