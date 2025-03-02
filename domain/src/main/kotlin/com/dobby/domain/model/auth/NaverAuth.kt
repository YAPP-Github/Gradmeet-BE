package com.dobby.domain.model.auth

data class NaverToken(
    val accessToken: String
)

data class NaverUserInfo(
    val email: String
)
