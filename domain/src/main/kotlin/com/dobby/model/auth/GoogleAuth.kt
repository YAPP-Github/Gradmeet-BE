package com.dobby.model.auth

data class GoogleToken(
    val accessToken: String
)

data class GoogleUserInfo(
    val email: String
)
