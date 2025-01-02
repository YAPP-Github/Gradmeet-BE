package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.OauthService
import com.dobby.backend.presentation.api.dto.request.GoogleTokenRequest
import com.dobby.backend.presentation.api.dto.request.OauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.OauthLoginResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "OAuth 로그인 API")
@RestController
@RequestMapping("/v1/auth")
class OauthController(
    private val oauthService: OauthService
) {

    @PostMapping("/oauth/login/google")
    @Operation(summary = "Google OAuth 로그인 API", description = "Google OAuth 로그인 후 인증 정보를 반환합니다")
    fun getUserDetails(
        @RequestBody @Valid oauthLoginRequest: OauthLoginRequest
    ): OauthLoginResponse {
        return oauthService.getGoogleUserInfo(oauthLoginRequest)
    }
}
