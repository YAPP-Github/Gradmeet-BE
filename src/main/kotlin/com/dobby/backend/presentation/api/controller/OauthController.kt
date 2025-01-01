package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.OauthService
import com.dobby.backend.domain.exception.OAuth2AuthenticationException
import com.dobby.backend.domain.exception.OAuth2EmailNotFoundException
import com.dobby.backend.domain.exception.OAuth2NameNotFoundException
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType.*
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.OauthUserDto
import com.dobby.backend.presentation.api.dto.response.OauthTokenResponse
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class OauthController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val oauthService: OauthService
) {

    @GetMapping("/oauth/login")
    fun getUserDetails(@RequestHeader("Authorization") authHeader: String): OauthTokenResponse{
        val accessToken = authHeader.substring(7)
        val userInfo = oauthService.getGoogleUserInfo(accessToken)
        val email = userInfo["email"] as? String ?: throw OAuth2EmailNotFoundException()
        val name = userInfo["name"] as? String ?: throw OAuth2NameNotFoundException()
        return OauthTokenResponse(
            jwtToken= accessToken,
            email = email,
            name = name,
            provider = GOOGLE
        )
    }
}
