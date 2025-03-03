package com.dobby.token

interface SecurityManager {
    fun generateAccessToken(memberId: String): String
    fun generateRefreshToken(memberId: String): String
    fun parseAuthentication(token: String): SecurityUser
    fun getMemberIdFromRefreshToken(refreshToken: String): String
    fun getMemberIdFromAccessToken(accessToken: String): String
}
