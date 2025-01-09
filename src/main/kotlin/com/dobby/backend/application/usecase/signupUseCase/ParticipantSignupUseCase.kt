package com.dobby.backend.application.usecase.signupUseCase

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.ParticipantGateway
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.database.entity.enum.*
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import java.time.LocalDate

class ParticipantSignupUseCase (
    private val participantGateway: ParticipantGateway,
    private val tokenGateway: TokenGateway
): UseCase<ParticipantSignupUseCase.Input, ParticipantSignupUseCase.Output>
{
    data class Input (
        val oauthEmail: String,
        val provider: ProviderType,
        val contactEmail: String,
        val name : String,
        val gender: GenderType,
        val birthDate: LocalDate,
        var basicAddressInfo: AddressInfo,
        var additionalAddressInfo: AddressInfo?,
        var preferType: MatchType,
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
        val memberId: Long?,
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
            memberInfo = SignupMapper.modelToParticipantRes(newParticipant)
        )
    }


    private fun createParticipant(input: Input): Participant {
        val member = Member(
            id = 0L,
            oauthEmail = input.oauthEmail,
            contactEmail = input.contactEmail,
            provider = input.provider,
            role = RoleType.PARTICIPANT,
            name = input.name,
            status = MemberStatus.ACTIVE
        )

        return Participant(
            id = 0L,
            member = member,
            gender = input.gender,
            birthDate = input.birthDate,
            basicAddressInfo = Participant.AddressInfo(
                region = input.basicAddressInfo.region,
                area = input.basicAddressInfo.area
            ),
            additionalAddressInfo = input.additionalAddressInfo?.let {
                Participant.AddressInfo(region = it.region, area = it.area)
            },
            preferType = input.preferType
        )
    }
}
