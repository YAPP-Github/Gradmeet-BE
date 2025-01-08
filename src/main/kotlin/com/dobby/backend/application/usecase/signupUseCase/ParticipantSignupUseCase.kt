package com.dobby.backend.application.usecase.signupUseCase

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.infrastructure.database.repository.ParticipantRepository
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.response.MemberResponse
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import com.dobby.backend.util.AuthenticationUtils

class ParticipantSignupUseCase (
    private val participantRepository: ParticipantRepository,
    private val jwtTokenProvider: JwtTokenProvider
): UseCase<ParticipantSignupRequest, SignupResponse>
{
    override fun execute(input: ParticipantSignupRequest): SignupResponse {
        val memberEntity = SignupMapper.toParticipantMember(input)
        val participantEntity = SignupMapper.toParticipant(memberEntity, input)

        val newParticipant = participantRepository.save(participantEntity)
        val authentication = AuthenticationUtils.createAuthentication(memberEntity)
        val accessToken = jwtTokenProvider.generateAccessToken(authentication)
        val refreshToken = jwtTokenProvider.generateRefreshToken(authentication)

        return SignupResponse(
            memberInfo = MemberResponse.fromDomain(newParticipant.member.toDomain()),
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
