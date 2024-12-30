package com.dobby.backend.application.service

import com.dobby.backend.domain.exception.AlreadyMemberException
import com.dobby.backend.domain.exception.MemberNotFoundException
import com.dobby.backend.infrastructure.database.entity.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.OauthUserDto
import com.dobby.backend.presentation.api.dto.request.ParticipantSignupRequest
import com.dobby.backend.util.AuthenticationUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Transactional
    fun login(oauthUserDto: OauthUserDto): String{
        val member= memberRepository.findByOauthEmailAndStatus(oauthUserDto.email, MemberStatus.ACTIVE)
            ?: throw MemberNotFoundException()
        val authentication = AuthenticationUtils.createAuthentication(member)
        return jwtTokenProvider.generateAccessToken(authentication)
    }
}