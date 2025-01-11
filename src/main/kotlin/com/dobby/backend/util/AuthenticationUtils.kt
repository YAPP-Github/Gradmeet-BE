package com.dobby.backend.util

import org.springframework.security.core.context.SecurityContextHolder

fun getCurrentMemberId() = SecurityContextHolder.getContext().authentication.name.toLong()
