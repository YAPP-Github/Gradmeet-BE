package com.dobby.backend.infrastructure.token

import com.dobby.backend.domain.exception.*
import com.dobby.backend.infrastructure.config.TokenProperties
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JWTTokenProvider(
    private val tokenProperties: TokenProperties
) {
    private final val signKey: SecretKey = SecretKeySpec(tokenProperties.secretKey.toByteArray(), "AES")
    private val jwtParser = Jwts
        .parser()
        .decryptWith(signKey)
        .build()

    fun generateAccessToken(authentication: Authentication): String {
        return Jwts.builder()
            .header()
            .add(TOKEN_TYPE_HEADER_KEY, ACCESS_TOKEN_TYPE_VALUE)
            .and()
            .claims()
            .add(MEMBER_ID_CLAIM_KEY, authentication.name)
            .and()
            .expiration(generateAccessTokenExpiration())
            .encryptWith(signKey, Jwts.ENC.A128CBC_HS256)
            .compact()
    }

    fun generateRefreshToken(authentication: Authentication): String {
        return Jwts.builder()
            .header()
            .add(TOKEN_TYPE_HEADER_KEY, REFRESH_TOKEN_TYPE_VALUE)
            .and()
            .claims()
            .add(MEMBER_ID_CLAIM_KEY, authentication.name)
            .and()
            .expiration(generateRefreshTokenExpiration())
            .encryptWith(signKey, Jwts.ENC.A128CBC_HS256)
            .compact()
    }

    fun parseAuthentication(accessToken: String): Authentication {
        try {
            val claims = jwtParser.parseEncryptedClaims(accessToken)
            val tokenType = claims.header[TOKEN_TYPE_HEADER_KEY] ?: throw InvalidTokenTypeException()
            if (tokenType != ACCESS_TOKEN_TYPE_VALUE) throw InvalidTokenTypeException()

            val memberId = claims.payload[MEMBER_ID_CLAIM_KEY] as? String ?: throw MemberNotFoundException()
            return UsernamePasswordAuthenticationToken(memberId, accessToken, emptyList())
        } catch (e: ExpiredJwtException) {
            throw AuthenticationTokenExpiredException()
        } catch (e: JwtException) {
            throw AuthenticationTokenNotValidException()
        }
    }

    fun getMemberIdFromRefreshToken(refreshToken: String): String {
        val claims = jwtParser.parseEncryptedClaims(refreshToken)
        val tokenType = claims.header[TOKEN_TYPE_HEADER_KEY] ?: throw RuntimeException()
        if (tokenType != REFRESH_TOKEN_TYPE_VALUE) throw RuntimeException()

        return claims.payload[MEMBER_ID_CLAIM_KEY] as? String ?: throw RuntimeException()
    }

    private fun generateAccessTokenExpiration() = Date(System.currentTimeMillis() + tokenProperties.accessExpiration * 1000)

    private fun generateRefreshTokenExpiration() = Date(System.currentTimeMillis() + tokenProperties.refreshExpiration * 1000)

    companion object {
        const val MEMBER_ID_CLAIM_KEY = "member_id"
        const val TOKEN_TYPE_HEADER_KEY = "token_type"
        const val ACCESS_TOKEN_TYPE_VALUE = "access_token"
        const val REFRESH_TOKEN_TYPE_VALUE = "refresh_token"
    }
}
