package com.dobby.token

import org.springframework.security.core.Authentication

interface JwtTokenManager {
    fun generateAccessToken(authentication: Authentication): String
    fun generateRefreshToken(authentication: Authentication): String
    fun parseAuthentication(token: String): Authentication
    fun getMemberIdFromRefreshToken(refreshToken: String): String
    fun getMemberIdFromAccessToken(accessToken: String): String
}
