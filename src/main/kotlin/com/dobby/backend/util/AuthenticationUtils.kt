package com.dobby.backend.util

import com.dobby.backend.infrastructure.database.entity.MemberEntity
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

object AuthenticationUtils {
    fun createAuthentication(member: MemberEntity): Authentication {
        return UsernamePasswordAuthenticationToken(
            member,
            null,
            emptyList()
        )
    }
}