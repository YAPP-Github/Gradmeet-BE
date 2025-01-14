package com.dobby.backend.presentation.api.dto.request.signup

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class EmailSendRequest (
    @Email(message = "학교 이메일이 유효하지 않습니다.")
    @NotNull(message = "학교 이메일은 공백일 수 없습니다.")
    @Schema(description = "대학교 이메일")
    val univEmail : String,
)
