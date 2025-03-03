package com.dobby.api.dto.response.auth

import com.dobby.api.dto.response.member.MemberResponse
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Getter

@Schema(description = "OAuth 로그인 결과 사용자 정보 DTO")
@Getter
data class OauthLoginResponse(
    @Schema(description = "OAuth 로그인 시, 이미 등록된 유저인지", example = "true")
    val isRegistered: Boolean,

    @Schema(description = "가입된 회원이라면 발급된 Access Token")
    val accessToken: String?,

    @Schema(description = "가입된 회원이라면 발급된 Refresh Token")
    val refreshToken: String?,

    @Schema(description = "사용자 정보")
    val memberInfo: MemberResponse
)
