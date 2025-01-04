package com.dobby.backend.presentation.api.dto.request.signup

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ResearcherSignupRequest(
    @Email(message = "OAuth 이메일이 유효하지 않습니다.")
    @NotBlank(message = "OAuth 이메일은 공백일 수 없습니다.")
    val oauthEmail: String,

    @NotBlank(message = "OAuth provider은 공백일 수 없습니다.")
    val provider: ProviderType,

    @Email(message= "연락 받을 이메일이 유효하지 않습니다.")
    @NotBlank(message = "연락 받을 이메일은 공백일 수 없습니다.")
    val contactEmail: String,

    @Email(message = "학교 이메일이 유효하지 않습니다.")
    @NotBlank(message = "학교 이메일은 공백일 수 없습니다.")
    val univEmail : String,

    @NotNull(message = "이메일 인증 여부는 공백일 수 없습니다.")
    var emailVerified: Boolean,

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    val name: String,

    @NotBlank(message = "학교명은 공백일 수 없습니다.")
    val univName: String,

    @NotBlank(message = "전공명은 공백일 수 없습니다.")
    val major: String,

    val labInfo: String?
)
