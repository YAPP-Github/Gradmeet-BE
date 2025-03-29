package com.dobby.api.dto.request.member

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class EmailVerificationRequest(
    @Email(message = "학교 이메일이 유효하지 않습니다.")
    @NotNull(message = "학교 이메일은 공백일 수 없습니다.")
    @Schema(description = "대학교 이메일")
    val univEmail: String,

    @NotNull(message = "인증 코드는 공백일 수 없습니다.")
    @Schema(description = "인증 코드")
    val inputCode: String
)
