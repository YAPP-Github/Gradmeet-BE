package com.dobby.api.dto.request.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "토큰 갱신 요청 DTO")
data class MemberRefreshTokenRequest(
    @NotBlank
    @Schema(description = "리프레시 토큰", example = "")
    val refreshToken: String,
)
