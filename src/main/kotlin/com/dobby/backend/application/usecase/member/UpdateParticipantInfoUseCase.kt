package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ContactEmailDuplicateException
import com.dobby.backend.domain.exception.ParticipantNotFoundException
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.ParticipantGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import java.time.LocalDate

class UpdateParticipantInfoUseCase(
    private val participantGateway: ParticipantGateway,
    private val memberGateway: MemberGateway
) : UseCase<UpdateParticipantInfoUseCase.Input, UpdateParticipantInfoUseCase.Output> {

    data class Input(
        val memberId: String,
        val contactEmail: String,
        val name: String,
        val basicAddressInfo: Participant.AddressInfo,
        val additionalAddressInfo: Participant.AddressInfo?,
        val matchType: MatchType?
    )

    data class Output(
        val member: Member,
        val gender: GenderType,
        val birthDate: LocalDate,
        val basicAddressInfo: Participant.AddressInfo,
        val additionalAddressInfo: Participant.AddressInfo?,
        val matchType: MatchType?
    )

    override fun execute(input: Input): Output {
        val participant = participantGateway.findByMemberId(input.memberId)
            ?: throw ParticipantNotFoundException
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

        return Output(
            member = updatedParticipant.member,
            gender = updatedParticipant.gender,
            birthDate = updatedParticipant.birthDate,
            basicAddressInfo = updatedParticipant.basicAddressInfo,
            additionalAddressInfo = updatedParticipant.additionalAddressInfo,
            matchType = updatedParticipant.matchType
        )
    }
}
