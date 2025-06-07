package com.dobby.api.dto.request.auth

import jakarta.validation.constraints.NotBlank

data class GoogleOauthLoginRequest(
    @NotBlank(message = "authorizationCode는 공백일 수 없습니다.")
    val authorizationCode: String
)
