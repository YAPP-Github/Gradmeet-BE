package com.dobby.backend.presentation.api.controller

<<<<<<< HEAD
import com.dobby.backend.domain.usecase.GenerateTestToken
import com.dobby.backend.domain.usecase.GenerateTokenWithRefreshToken
import com.dobby.backend.domain.usecase.GetMemberById
import com.dobby.backend.presentation.api.dto.request.MemberRefreshTokenRequest
import com.dobby.backend.presentation.api.dto.response.MemberResponse
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
    private val getMemberById: GetMemberById
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
        val member = getMemberById.execute(
            GetMemberById.Input(
                memberId = tokens.memberId,
            )
        )

        return MemberSignInResponse(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            member = MemberResponse.fromDomain(member)
        )
=======
import com.dobby.backend.application.service.OauthService
import com.dobby.backend.domain.usecase.GenerateTestToken
import com.dobby.backend.presentation.api.dto.request.OauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.OauthLoginResponse
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
    fun getUserDetails(
        @RequestBody @Valid oauthLoginRequest: OauthLoginRequest
    ): OauthLoginResponse {
        return oauthService.getGoogleUserInfo(oauthLoginRequest)
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> dc4d52e ([YS-31] feat: 구글 OAuth 로그인 구현 (#13))
=======
=======
=======
    }

>>>>>>> 6c4313b (refact: rename entity)
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
<<<<<<< HEAD
>>>>>>> 63d0863 (feat: add GenerateTokenWithRefreshToken)
>>>>>>> 3bac1d2 (feat: add GenerateTokenWithRefreshToken)
=======
>>>>>>> 6c4313b (refact: rename entity)
    }
}
