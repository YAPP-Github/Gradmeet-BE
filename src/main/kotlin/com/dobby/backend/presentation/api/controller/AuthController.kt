package com.dobby.backend.presentation.api.controller

import com.dobby.backend.domain.usecase.GenerateTestToken
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.response.TestMemberSignInResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "인증 관련 API")
@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val generateTestToken: GenerateTestToken,
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
}
