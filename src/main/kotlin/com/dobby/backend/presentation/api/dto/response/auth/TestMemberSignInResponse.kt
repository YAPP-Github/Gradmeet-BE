package com.dobby.backend.presentation.api.dto.response.auth

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "테스트용 로그인 결과 DTO")
data class TestMemberSignInResponse(
    @Schema(description = "엑세스 토큰")
    val accessToken: String,

    @Schema(description = "리프레쉬 토큰")
    val refreshToken: String
)
