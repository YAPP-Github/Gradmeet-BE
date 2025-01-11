package com.dobby.backend.util

import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

object AuthenticationUtils {
    fun createAuthentication(member: MemberEntity): Authentication {
        return UsernamePasswordAuthenticationToken(
            member,
            null,
            emptyList()
        ) as Authentication
    }
    fun getCurrentMemberId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.name.toLong()
    }
}
