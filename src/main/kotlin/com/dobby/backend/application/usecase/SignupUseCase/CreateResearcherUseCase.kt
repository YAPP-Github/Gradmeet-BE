package com.dobby.backend.application.usecase.SignupUseCase

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.infrastructure.database.repository.ResearcherRepository
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.MemberResponse
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import com.dobby.backend.util.AuthenticationUtils

class CreateResearcherUseCase(
    private val researcherRepository: ResearcherRepository,
    private val jwtTokenProvider: JwtTokenProvider
) : UseCase<ResearcherSignupRequest, SignupResponse> {
    override fun execute(input: ResearcherSignupRequest): SignupResponse {
        val memberEntity = SignupMapper.toResearcherMember(input)
        val newResearcher = SignupMapper.toResearcher(memberEntity, input)
        researcherRepository.save(newResearcher)

        val authentication = AuthenticationUtils.createAuthentication(memberEntity)
        val accessToken = jwtTokenProvider.generateAccessToken(authentication)
        val refreshToken = jwtTokenProvider.generateRefreshToken(authentication)

        return SignupResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            memberInfo = MemberResponse.fromDomain(newResearcher.member.toDomain())
        )
    }
}
