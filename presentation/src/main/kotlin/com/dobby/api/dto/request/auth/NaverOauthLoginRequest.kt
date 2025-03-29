package com.dobby.api.dto.request.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class NaverOauthLoginRequest(
    @NotBlank(message = "authorizationCode는 공백일 수 없습니다.")
    @Schema(description = "인가 코드")
    val authorizationCode: String,

    @NotBlank(message = "state는 공백일 수 없습니다.")
    @Schema(description = "state")
    val state: String
)
