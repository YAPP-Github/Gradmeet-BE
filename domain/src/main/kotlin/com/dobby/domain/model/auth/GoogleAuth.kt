package com.dobby.domain.model.auth

data class GoogleToken(
    val accessToken: String
)

data class GoogleUserInfo(
    val email: String
)
