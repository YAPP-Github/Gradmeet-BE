package com.dobby.usecase.member

import com.dobby.usecase.UseCase
import com.dobby.IdGenerator
import com.dobby.enums.MatchType
import com.dobby.gateway.member.ParticipantGateway
import com.dobby.gateway.auth.TokenGateway
import com.dobby.gateway.member.MemberConsentGateway
import com.dobby.model.member.Member
import com.dobby.model.member.MemberConsent
import com.dobby.model.member.Participant
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.member.GenderType
import com.dobby.enums.member.ProviderType
import com.dobby.enums.member.RoleType
import java.time.LocalDate

class CreateParticipantUseCase (
    private val participantGateway: ParticipantGateway,
    private val memberConsentGateway: MemberConsentGateway,
    private val tokenGateway: TokenGateway,
    private val idGenerator: IdGenerator
): UseCase<CreateParticipantUseCase.Input, CreateParticipantUseCase.Output>
{
    data class Input (
        val oauthEmail: String,
        val provider: ProviderType,
        val contactEmail: String,
        val name : String,
        val gender: GenderType,
        val birthDate: LocalDate,
        var basicAddressInfo: AddressInfo,
        var additionalAddressInfo: AddressInfo,
        var matchType: MatchType?,
        var adConsent: Boolean,
        var matchConsent: Boolean
    )

    data class AddressInfo(
        val region: Region,
        val area: Area
    )

    data class Output(
        val accessToken: String,
        val refreshToken: String,
        val memberInfo: MemberResponse
    )
    data class MemberResponse(
        val memberId: String?,
        val name: String?,
        val oauthEmail: String?,
        val provider: ProviderType?,
        val contactEmail: String?,
        val role: RoleType?,
    )

    override fun execute(input: Input): Output {
        val participant = createParticipant(input)

        val newParticipant = participantGateway.save(participant)

        val newMember = newParticipant.member

        val accessToken = tokenGateway.generateAccessToken(newMember)
        val refreshToken = tokenGateway.generateRefreshToken(newMember)

        return Output(
            accessToken = accessToken,
            refreshToken = refreshToken,
            memberInfo = MemberResponse(
                memberId = newMember.id,
                name = newMember.name,
                oauthEmail = newMember.oauthEmail,
                provider = newMember.provider,
                contactEmail = newMember.contactEmail,
                role = newMember.role
            )
        )
    }

    private fun createParticipant(input: Input): Participant {
        val member = Member.newMember(
            id = idGenerator.generateId(),
            oauthEmail = input.oauthEmail,
            contactEmail = input.contactEmail,
            provider = input.provider,
            role = RoleType.PARTICIPANT,
            name = input.name,
        )

        val participant= Participant.newParticipant(
            id = idGenerator.generateId(),
            member = member,
            gender = input.gender,
            birthDate = input.birthDate,
            basicAddressInfo = Participant.AddressInfo(
                region = input.basicAddressInfo.region,
                area = input.basicAddressInfo.area
            ),
            additionalAddressInfo = Participant.AddressInfo(
                region = input.additionalAddressInfo.region,
                area = input.additionalAddressInfo.area,
            ),
            matchType = input.matchType
        )
        participantGateway.save(participant)

        val memberConsent = MemberConsent.newConsent(
            memberId = member.id,
            adConsent = input.adConsent,
            matchConsent = input.matchConsent,
        )
        memberConsentGateway.save(memberConsent)

        return participant
    }
}
