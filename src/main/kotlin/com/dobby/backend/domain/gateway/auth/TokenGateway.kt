package com.dobby.backend.domain.gateway.auth

import com.dobby.backend.domain.model.member.Member

interface TokenGateway {
    fun generateAccessToken(member: Member): String
    fun generateRefreshToken(member: Member): String
    fun extractMemberIdFromRefreshToken(token: String): String
    fun extractMemberIdFromAccessToken(token: String): String
}
