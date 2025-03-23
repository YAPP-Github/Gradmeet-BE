package com.dobby.security.token

import com.dobby.security.SecurityManager
import com.dobby.security.SecurityUser
import org.springframework.stereotype.Component

@Component
class JwtTokenManager(
    private val jwtTokenProvider: JwtTokenProvider
) : SecurityManager {

    override fun parseAuthentication(token: String): SecurityUser {
        val authentication = jwtTokenProvider.parseAuthentication(token)
        val memberId = authentication.name
        val roles = authentication.authorities.map { it.authority }
        return SecurityUser(memberId, roles)
    }
}
