package com.dobby.token

data class SecurityUser(
    val memberId: String,
    val roles: List<String>
)
