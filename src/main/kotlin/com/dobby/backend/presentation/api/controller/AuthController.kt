package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.OauthService
import com.dobby.backend.application.usecase.GenerateTestToken
import com.dobby.backend.presentation.api.dto.request.OauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import com.dobby.backend.application.usecase.GenerateTokenWithRefreshToken
import com.dobby.backend.application.usecase.GetMemberById
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.presentation.api.dto.request.MemberRefreshTokenRequest
import com.dobby.backend.presentation.api.dto.response.MemberResponse
import com.dobby.backend.presentation.api.dto.response.TestMemberSignInResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "인증 관련 API")
@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val generateTestToken: GenerateTestToken,
    private val generateTokenWithRefreshToken: GenerateTokenWithRefreshToken,
    private val getMemberById: GetMemberById,
    private val oauthService: OauthService,
) {
    @Operation(summary = "테스트용 토큰 강제 발급", description = "memberId로 테스트용 토큰을 발급합니다")
    @PostMapping("/force-token")
    fun forceToken(
        @RequestParam memberId: Long
    ): TestMemberSignInResponse {
        val result = generateTestToken.execute(
            GenerateTestToken.Input(memberId)
        )

        return TestMemberSignInResponse(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken
        )
    }

    @PostMapping("/login/google")
    @Operation(summary = "Google OAuth 로그인 API", description = "Google OAuth 로그인 후 인증 정보를 반환합니다")
    fun signInWithGoogle(
        @RequestParam role : RoleType, // RESEARCHER, PARTICIPANT
        @RequestBody @Valid oauthLoginRequest: OauthLoginRequest
    ): OauthLoginResponse {
        return oauthService.getGoogleUserInfo(oauthLoginRequest)
    }

    @Operation(summary = "토큰 갱신 요청", description = "리프레시 토큰으로 기존 토큰을 갱신합니다")
    @PostMapping("/refresh")
    fun signInWithRefreshToken(
        @RequestBody @Valid request: MemberRefreshTokenRequest,
    ): OauthLoginResponse {
        val tokens = generateTokenWithRefreshToken.execute(
            GenerateTokenWithRefreshToken.Input(
                refreshToken = request.refreshToken,
            )
        )
        val member = getMemberById.execute(
            GetMemberById.Input(
                memberId = tokens.memberId,
            )
        )

        return OauthLoginResponse(
            isRegistered = false,
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            memberInfo = MemberResponse.fromDomain(member)
        )
    }
}
