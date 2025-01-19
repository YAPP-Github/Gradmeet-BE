package com.dobby.backend.util

import org.springframework.security.core.context.SecurityContextHolder

fun getCurrentMemberId() = SecurityContextHolder.getContext().authentication.name.toLong()

fun getCurrentMemberIdOrNull(): Long? {
    val authentication = SecurityContextHolder.getContext().authentication
    val name = authentication?.name

    return if (name != null && name != "anonymousUser") {
        name.toLong()
    } else {
        null
    }
}
