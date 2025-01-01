package com.dobby.backend.presentation.api.config.handler

import com.dobby.backend.application.service.MemberService
import com.dobby.backend.domain.exception.OAuth2AuthenticationException
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
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
        authentication: Authentication
    ) {
        println("[DEBUG] OauthSuccessHandler TRIGGERED!") // 로그 추가
        println("[DEBUG] Authentication class: ${authentication::class.java}")
        println("[DEBUG] Authentication details: ${authentication.details}")
        println("[DEBUG] Authentication principal: ${authentication.principal}")

        val oauth2Token = authentication as? OAuth2AuthenticationToken
            ?: throw OAuth2AuthenticationException()

        val principal = oauth2Token.principal
        val attributes = principal.attributes

        val email = attributes["email"] as? String ?: throw IllegalStateException("Email not found")
        val name = attributes["name"] as? String ?: throw IllegalStateException("Name not found")

//        val oauthUserDto = OauthUserDto(email = email, name = name, provider = ProviderType.GOOGLE)
//        val member = memberService.login(oauthUserDto)
//
//        val newAuthentication = AuthenticationUtils.createAuthentication(member)
//        SecurityContextHolder.getContext().authentication = newAuthentication

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
