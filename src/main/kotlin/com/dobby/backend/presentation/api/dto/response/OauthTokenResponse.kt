package com.dobby.backend.presentation.api.dto.response

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "OAuth 로그인 결과 사용자 정보 DTO")
data class OauthTokenResponse(
    @Schema(description = "OAuth 토큰")
    val jwtToken: String,

    @Schema(description = "OAuth 이메일")
    val email: String,

    @Schema(description = "OAuth 이름")
    val name: String,

    @Schema(description = "OAuth 제공자 (GOOGLE, NAVER)")
    val provider: ProviderType
)
