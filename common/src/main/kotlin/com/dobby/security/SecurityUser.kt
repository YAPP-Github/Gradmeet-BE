package com.dobby.security

data class SecurityUser(
    val memberId: String,
    val roles: List<String>
)
