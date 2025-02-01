package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.member.ParticipantGateway
import com.dobby.backend.domain.gateway.auth.TokenGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.database.entity.enums.*
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import java.time.LocalDate
import java.time.LocalDateTime

class CreateParticipantUseCase (
    private val participantGateway: ParticipantGateway,
    private val tokenGateway: TokenGateway
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
                role = newMember.role
            )
        )
    }

    private fun createParticipant(input: Input): Participant {
        val member = Member.newMember(
            oauthEmail = input.oauthEmail,
            contactEmail = input.contactEmail,
            provider = input.provider,
            role = RoleType.PARTICIPANT,
            name = input.name,
        )

        return Participant.newParticipant(
            member = member,
            gender = input.gender,
            birthDate = input.birthDate,
            basicAddressInfo = Participant.AddressInfo(
                region = input.basicAddressInfo.region,
                area = input.basicAddressInfo.area
            ),
            additionalAddressInfo = Participant.AddressInfo(
                region = input.additionalAddressInfo.region,
                area = input.additionalAddressInfo.area
            ),
            matchType = input.matchType
        )
    }
}
