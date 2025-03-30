package com.dobby.api.dto.response.member

import io.swagger.v3.oas.annotations.media.Schema

data class SignUpResponse(
    @Schema(description = "회원가입 후 발급되는 Access Token")
    val accessToken: String,

    @Schema(description = "회원가입 후 발급되는 Refresh Token")
    val refreshToken: String,

    @Schema(description = "회원가입 정보")
    val memberInfo: MemberResponse
)
