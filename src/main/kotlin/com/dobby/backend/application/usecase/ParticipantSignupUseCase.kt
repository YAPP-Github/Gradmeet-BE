package com.dobby.backend.application.usecase

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.infrastructure.database.repository.ParticipantRepository
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.response.MemberResponse
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import com.dobby.backend.util.AuthenticationUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
class ParticipantSignupUseCase (
    private val participantRepository: ParticipantRepository,
    private val jwtTokenProvider: JwtTokenProvider
):UseCase<ParticipantSignupRequest, SignupResponse>
{
    @Transactional
    override fun execute(input: ParticipantSignupRequest): SignupResponse {
        val memberEntity = SignupMapper.toMember(input)
        val participantEntity = SignupMapper.toParticipant(memberEntity, input)

        val newParticipant = participantRepository.save(participantEntity)

        val accessToken = jwtTokenProvider.generateAccessToken(AuthenticationUtils.createAuthentication(memberEntity))
        val refreshToken = jwtTokenProvider.generateRefreshToken(AuthenticationUtils.createAuthentication(memberEntity))

        return SignupResponse(
            memberInfo = MemberResponse.fromDomain(newParticipant.member.toDomain()),
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}