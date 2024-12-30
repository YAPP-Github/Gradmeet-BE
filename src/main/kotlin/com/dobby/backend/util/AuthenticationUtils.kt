package com.dobby.backend.util

import com.dobby.backend.infrastructure.database.entity.Member
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

object AuthenticationUtils {
    fun createAuthentication(member: Member): Authentication {
        return UsernamePasswordAuthenticationToken(
            member.id.toString(),
            null,
            emptyList()
        ) as Authentication
    }
}