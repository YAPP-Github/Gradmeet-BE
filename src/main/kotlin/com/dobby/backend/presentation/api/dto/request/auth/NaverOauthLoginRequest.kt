package com.dobby.backend.presentation.api.dto.request.auth

import jakarta.validation.constraints.NotBlank

data class NaverOauthLoginRequest(
    @NotBlank(message = "authorizationCode는 공백일 수 없습니다.")
    val authorizationCode: String,

    @NotBlank(message = "state는 공백일 수 없습니다.")
    val state: String,
)
