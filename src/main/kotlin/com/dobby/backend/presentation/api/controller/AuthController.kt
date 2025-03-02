package com.dobby.backend.presentation.api.controller

import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import com.dobby.enums.member.RoleType
import com.dobby.backend.presentation.api.dto.request.auth.GoogleOauthLoginRequest
import com.dobby.backend.presentation.api.dto.request.auth.MemberRefreshTokenRequest
import com.dobby.backend.presentation.api.dto.request.auth.NaverOauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.auth.TestMemberSignInResponse
import com.dobby.backend.presentation.api.mapper.AuthMapper
import com.dobby.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "인증 API - /v1/auth ")
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
        val input = AuthMapper.toGoogleOauthLoginInput(request, role)
        val output = authService.getGoogleUserInfo(input)
        return AuthMapper.toGoogleOauthLoginResponse(output)
    }

    @PostMapping("/login/naver")
    @Operation(summary = "Naver OAuth 로그인 API", description = "Naver OAuth 로그인 후 인증 정보를 반환합니다")
    fun signInWithNaver(
        @RequestParam role : RoleType,
        @RequestBody @Valid request: NaverOauthLoginRequest
    ): OauthLoginResponse {
        val input = AuthMapper.toNaverOauthLoginInput(request, role)
        val output = authService.getNaverUserInfo(input)
        return AuthMapper.toNaverOauthLoginResponse(output)
    }

    @Operation(summary = "테스트용 토큰 강제 발급", description = "memberId로 테스트용 토큰을 발급합니다")
    @PostMapping("/force-token")
    fun forceToken(
        @RequestParam memberId: String
    ): TestMemberSignInResponse {
        val input = AuthMapper.toForceTokenInput(memberId)
        val output = authService.forceToken(input)
        return AuthMapper.toTestMemberSignInResponse(output)
    }

    @Operation(summary = "토큰 갱신 요청", description = "리프레시 토큰으로 기존 토큰을 갱신합니다")
    @PostMapping("/refresh")
    fun signInWithRefreshToken(
        @RequestBody @Valid request: MemberRefreshTokenRequest,
    ): OauthLoginResponse {
        val input = AuthMapper.toSignInWithRefreshTokenInput(request)
        val output = authService.signInWithRefreshToken(input)
        return AuthMapper.toSignInWithRefreshTokenResponse(output)
    }
}
