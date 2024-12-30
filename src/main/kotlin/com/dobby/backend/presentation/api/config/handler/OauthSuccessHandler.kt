package com.dobby.backend.presentation.api.config.handler

import com.dobby.backend.application.mapper.OauthUserMapper
import com.dobby.backend.application.service.MemberService
import com.dobby.backend.domain.exception.OAuth2AuthenticationException
import com.dobby.backend.domain.exception.OAuth2ProviderNotSupportedException
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OauthSuccessHandler (
    private val memberService: MemberService,
    private val jwtTokenProvider: JwtTokenProvider
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        val oauthUser = authentication?.principal as? OAuth2User
            ?: throw OAuth2AuthenticationException()
        val registrationId = request.getParameter("registrationId")
            ?: throw OAuth2AuthenticationException()

        val provider = when(registrationId.lowercase()) {
            "google" -> ProviderType.GOOGLE
            "naver" -> ProviderType.NAVER
            else -> throw OAuth2ProviderNotSupportedException()
        }

        val oauthUserDto = OauthUserMapper.toDto(oauthUser, provider)
        val member = memberService.login(oauthUserDto)

        val jwtToken = jwtTokenProvider.generateAccessToken(authentication)

        response.apply {
            contentType = "application/json"
            characterEncoding = "UTF-8"
            writer.write("""
                {
                    "accessToken": "$jwtToken",
                    "message": "Authentication successful"
                }
            """.trimIndent()
            )
            writer.flush()
        }
    }
}
