package com.dobby.backend.presentation.api.controller

import com.dobby.backend.domain.usecase.GenerateTestToken
import com.dobby.backend.domain.usecase.GenerateTokenWithRefreshToken
import com.dobby.backend.presentation.api.dto.request.MemberRefreshTokenRequest
import com.dobby.backend.presentation.api.dto.response.MemberSignInResponse
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

    @Operation(summary = "토큰 갱신 요청", description = "리프레시 토큰으로 기존 토큰을 갱신합니다")
    @PostMapping("/refresh")
    fun loginWithRefreshToken(
        @RequestBody @Valid request: MemberRefreshTokenRequest,
    ): MemberSignInResponse {
        val tokens = generateTokenWithRefreshToken.execute(
            GenerateTokenWithRefreshToken.Input(
                refreshToken = request.refreshToken,
            )
        )
        return MemberSignInResponse(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            memberId = tokens.memberId
        )
    }
}
