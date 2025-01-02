package com.dobby.backend.presentation.api.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 결과 DTO")
data class MemberSignInResponse(
    @Schema(description = "엑세스 토큰")
    val accessToken: String,

    @Schema(description = "리프레쉬 토큰")
    val refreshToken: String,

    @Schema(description = "사용자 Id")
    val memberId: Long
)
