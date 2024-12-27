package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.domain.model.Member
import com.dobby.backend.infrastructure.token.JWTTokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component

@Component
class TokenGatewayImpl(
    private val tokenProvider: JWTTokenProvider,
) : TokenGateway {
    override fun generateAccessToken(member: Member): String {
        val authentication = UsernamePasswordAuthenticationToken(
            member.memberId,
            null,
        )
        return tokenProvider.generateAccessToken(authentication)
    }

    override fun generateRefreshToken(member: Member): String {
        val authentication = UsernamePasswordAuthenticationToken(
            member.memberId,
            null,
        )
        return tokenProvider.generateRefreshToken(authentication)
    }

    override fun extractMemberIdFromRefreshToken(token: String): String {
        return tokenProvider.getMemberIdFromRefreshToken(token)
    }
}
