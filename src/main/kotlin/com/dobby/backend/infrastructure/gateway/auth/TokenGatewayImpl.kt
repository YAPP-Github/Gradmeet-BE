package com.dobby.backend.infrastructure.gateway.auth

import com.dobby.backend.domain.gateway.auth.TokenGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class TokenGatewayImpl(
    private val tokenProvider: JwtTokenProvider,
) : TokenGateway {
    override fun generateAccessToken(member: Member): String {
        val authorities = listOf(SimpleGrantedAuthority(member.role?.roleName))
        val authentication = UsernamePasswordAuthenticationToken(
            member.id,
            null,
            authorities
        )
        return tokenProvider.generateAccessToken(authentication)
    }

    override fun generateRefreshToken(member: Member): String {
        val authentication = UsernamePasswordAuthenticationToken(
            member.id,
            null
        )
        return tokenProvider.generateRefreshToken(authentication)
    }

    override fun extractMemberIdFromRefreshToken(token: String): String {
        return tokenProvider.getMemberIdFromRefreshToken(token)
    }

    override fun extractMemberIdFromAccessToken(token: String): String {
        return tokenProvider.getMemberIdFromAccessToken(token)
    }

}
