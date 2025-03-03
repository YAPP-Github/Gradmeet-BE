package com.dobby.util

import org.springframework.security.core.context.SecurityContextHolder

fun getCurrentMemberId(): String = SecurityContextHolder.getContext().authentication.name

fun getCurrentMemberIdOrNull(): String? {
    return SecurityContextHolder.getContext().authentication?.name?.takeIf { it != "anonymousUser" }?.let { it }
}
