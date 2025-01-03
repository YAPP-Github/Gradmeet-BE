package com.dobby.backend.presentation.api.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Getter

@Schema(description = "OAuth 로그인 결과 사용자 정보 DTO")
@Getter
data class OauthLoginResponse(
    @Schema(description = "OAuth 로그인 시, 이미 등록된 유저인지", example = "true")
    val isRegistered: Boolean,

    @Schema(description = "Access Token")
    val accessToken: String,

    @Schema(description = "Refresh Token")
    val refreshToken: String,

    @Schema(description = "사용자 정보")
    val memberInfo: MemberResponse
) {
}
