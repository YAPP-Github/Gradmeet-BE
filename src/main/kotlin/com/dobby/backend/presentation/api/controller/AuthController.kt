package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.AuthService
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.presentation.api.dto.request.auth.GoogleOauthLoginRequest
import com.dobby.backend.presentation.api.dto.request.auth.MemberRefreshTokenRequest
import com.dobby.backend.presentation.api.dto.request.auth.NaverOauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.member.MemberResponse
import com.dobby.backend.presentation.api.dto.response.auth.TestMemberSignInResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "인증 관련 API - /v1/auth ")
@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login/google")
    @Operation(summary = "Google OAuth 로그인 API", description = "Google OAuth 로그인 후 인증 정보를 반환합니다")
    fun signInWithGoogle(
        @RequestParam role : RoleType,
        @RequestBody @Valid request: GoogleOauthLoginRequest
    ): OauthLoginResponse {
        val result = authService.getGoogleUserInfo(request.authorizationCode)
        return OauthLoginResponse(
            isRegistered = result.isRegistered,
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
            memberInfo = MemberResponse(
                memberId = result.memberId,
                name = result.name,
                oauthEmail = result.oauthEmail,
                role = result.role,
                provider = result.provider,
            )
        )
    }

    @PostMapping("/login/naver")
    @Operation(summary = "Naver OAuth 로그인 API", description = "Naver OAuth 로그인 후 인증 정보를 반환합니다")
    fun signInWithNaver(
        @RequestParam role : RoleType,
        @RequestBody @Valid request: NaverOauthLoginRequest
    ): OauthLoginResponse {
        val result = authService.getNaverUserInfo(request.authorizationCode, request.state)
        return OauthLoginResponse(
            isRegistered = result.isRegistered,
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
            memberInfo = MemberResponse(
                memberId = result.memberId,
                name = result.name,
                oauthEmail = result.oauthEmail,
                role = result.role,
                provider = result.provider,
            )
        )
    }

    @Operation(summary = "테스트용 토큰 강제 발급", description = "memberId로 테스트용 토큰을 발급합니다")
    @PostMapping("/force-token")
    fun forceToken(
        @RequestParam memberId: Long
    ): TestMemberSignInResponse {
        val result = authService.forceToken(memberId)
        return TestMemberSignInResponse(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken
        )
    }

    @Operation(summary = "토큰 갱신 요청", description = "리프레시 토큰으로 기존 토큰을 갱신합니다")
    @PostMapping("/refresh")
    fun signInWithRefreshToken(
        @RequestBody @Valid request: MemberRefreshTokenRequest,
    ): OauthLoginResponse {
        val result = authService.signInWithRefreshToken(request.refreshToken)
        return OauthLoginResponse(
            isRegistered = false,
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
            memberInfo = MemberResponse.fromDomain(result.member)
        )
    }
}
