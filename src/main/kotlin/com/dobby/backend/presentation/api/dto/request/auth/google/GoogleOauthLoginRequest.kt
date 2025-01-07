package com.dobby.backend.presentation.api.dto.request.auth.google

import jakarta.validation.constraints.NotBlank

data class GoogleOauthLoginRequest(
    @NotBlank(message = "authorizationCode는 공백일 수 없습니다.")
    val authorizationCode: String
)
