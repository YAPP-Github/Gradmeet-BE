package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.OauthService
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.response.OauthTokenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "OAuth 로그인 관련 API")
@RestController
@RequestMapping("/v1/auth")
class OauthController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val oauthService: OauthService
) {

    @GetMapping("/oauth/login")
    @Operation(summary = "Google OAuth 로그인 API", description = "Google OAuth 로그인 후 인증 정보를 반환합니다")
    fun getUserDetails(
        @Parameter(
            description = "Bearer Token for Google OAuth- OAuth 로그인 이후 [Execute] 버튼을 누르면 자동으로 추가됩니다.",
            required = true,
            example = "Bearer <AccessToken>"
        )
        @RequestHeader("Authorization") authHeader: String
    ): OauthTokenResponse {
        println("Authorization Header: $authHeader")
        val accessToken = authHeader.substring(7)
        return oauthService.getGoogleUserInfo(accessToken)
    }
}
