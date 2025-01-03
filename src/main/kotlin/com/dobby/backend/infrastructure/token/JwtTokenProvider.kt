package com.dobby.backend.infrastructure.token

import com.dobby.backend.domain.exception.*
import com.dobby.backend.infrastructure.config.properties.TokenProperties
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
@EnableConfigurationProperties(TokenProperties::class)
class JwtTokenProvider(
    private val tokenProperties: TokenProperties
) {
    private final val signKey: SecretKey = SecretKeySpec(tokenProperties.secretKey.toByteArray(), "AES")
    private val jwtParser = Jwts
        .parser()
        .decryptWith(signKey)
        .build()

    fun generateAccessToken(authentication: Authentication): String {
        return generateToken(
            tokenType = ACCESS_TOKEN_TYPE_VALUE,
            authentication = authentication,
            expirationDate = generateAccessTokenExpiration()
        )
    }

    fun generateRefreshToken(authentication: Authentication): String {
        return generateToken(
            tokenType = REFRESH_TOKEN_TYPE_VALUE,
            authentication = authentication,
            expirationDate = generateAccessTokenExpiration()
        )
    }

    private fun generateToken(
        tokenType: String,
        authentication: Authentication,
        expirationDate: Date
    ): String {
        val authorities = authentication.authorities.joinToString(",") {
            it.authority
        }

        return Jwts.builder()
            .header().add(TOKEN_TYPE_HEADER_KEY, tokenType)
            .and()
            .claims()
            .add(MEMBER_ID_CLAIM_KEY, authentication.name)
            .add(AUTHORITIES_CLAIM_KEY, authorities)
            .and()
            .expiration(expirationDate)
            .encryptWith(signKey, Jwts.ENC.A128CBC_HS256)
            .compact()
    }

    fun parseAuthentication(accessToken: String): Authentication {
        try {
            val claims = jwtParser.parseEncryptedClaims(accessToken)
            val tokenType = claims.header[TOKEN_TYPE_HEADER_KEY] ?: throw InvalidTokenTypeException()
            if (tokenType != ACCESS_TOKEN_TYPE_VALUE) throw InvalidTokenTypeException()

            val memberId = claims.payload[MEMBER_ID_CLAIM_KEY] as? String ?: throw MemberNotFoundException()
            val authorities = claims.payload[AUTHORITIES_CLAIM_KEY]?.toString()
                ?.split(",")
                ?.map { SimpleGrantedAuthority(it) }
                ?: emptyList()

            return UsernamePasswordAuthenticationToken(memberId, accessToken, authorities)
        } catch (e: ExpiredJwtException) {
            throw AuthenticationTokenExpiredException()
        } catch (e: JwtException) {
            throw AuthenticationTokenNotValidException()
        }
    }

    fun getMemberIdFromRefreshToken(refreshToken: String): String {
        return try {
            val claims = jwtParser.parseEncryptedClaims(refreshToken)
            val tokenType = claims.header[TOKEN_TYPE_HEADER_KEY]
            if (tokenType != REFRESH_TOKEN_TYPE_VALUE) {
                throw InvalidTokenTypeException()
            }

            claims.payload[MEMBER_ID_CLAIM_KEY] as? String ?: throw InvalidTokenValueException()
        } catch (e: Exception) {
            throw InvalidTokenValueException()
        }
    }

    private fun generateAccessTokenExpiration() = Date(System.currentTimeMillis() + tokenProperties.expiration.access * 1000)

    private fun generateRefreshTokenExpiration() = Date(System.currentTimeMillis() + tokenProperties.expiration.refresh * 1000)

    companion object {
        const val MEMBER_ID_CLAIM_KEY = "member_id"
        const val AUTHORITIES_CLAIM_KEY = "authorities"
        const val TOKEN_TYPE_HEADER_KEY = "token_type"
        const val ACCESS_TOKEN_TYPE_VALUE = "access_token"
        const val REFRESH_TOKEN_TYPE_VALUE = "refresh_token"
    }
}
