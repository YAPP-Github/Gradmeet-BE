package com.dobby.backend.presentation.api.dto.response.signup

import io.swagger.v3.oas.annotations.media.Schema

data class EmailVerificationResponse(
    @Schema(description = "학교 이메일 인증이 성공했는지 여부입니다.")
    val isSuccess: Boolean,

    @Schema(description = "인증 코드 인증 성공 메시지 입니다.")
    val message : String
)
