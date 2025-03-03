package com.dobby.token

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtTokenManager(
    private val jwtTokenProvider: JwtTokenProvider
) : SecurityManager {

    override fun generateAccessToken(memberId: String): String {
        val authentication = createAuthentication(memberId)
        return jwtTokenProvider.generateAccessToken(authentication)
    }

    override fun generateRefreshToken(memberId: String): String {
        val authentication = createAuthentication(memberId)
        return jwtTokenProvider.generateRefreshToken(authentication)
    }

    override fun parseAuthentication(token: String): SecurityUser {
        val authentication = jwtTokenProvider.parseAuthentication(token)
        val memberId = authentication.name
        val roles = authentication.authorities.map { it.authority }
        return SecurityUser(memberId, roles)
    }

    override fun getMemberIdFromRefreshToken(refreshToken: String): String {
        return jwtTokenProvider.getMemberIdFromRefreshToken(refreshToken)
    }

    override fun getMemberIdFromAccessToken(accessToken: String): String {
        return jwtTokenProvider.getMemberIdFromAccessToken(accessToken)
    }

    private fun createAuthentication(memberId: String): Authentication {
        return UsernamePasswordAuthenticationToken(memberId, null, listOf(SimpleGrantedAuthority("ROLE_USER")))
    }
}
