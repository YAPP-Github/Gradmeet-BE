package com.dobby.backend.presentation.api.dto.response.signup

import com.dobby.backend.presentation.api.dto.response.MemberResponse
import io.swagger.v3.oas.annotations.media.Schema

data class SignupResponse(
    @Schema(description = "회원가입 후 발급되는 Access Token")
    val accessToken: String,

    @Schema(description = "회원가입 후 발급되는 Refresh Token")
    val refreshToken: String,

    @Schema(description = "회원가입 정보")
    val memberInfo: MemberResponse
)
