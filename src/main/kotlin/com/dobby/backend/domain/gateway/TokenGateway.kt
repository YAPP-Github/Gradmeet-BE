package com.dobby.backend.domain.gateway

interface TokenGateway {
    fun generateAccessToken(memberId: Long): String
    fun generateRefreshToken(memberId: Long): String
    fun extractMemberIdFromRefreshToken(token: String): String
}
