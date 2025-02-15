package com.dobby.backend.presentation.api.dto.request.member

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UpdateResearcherInfoRequest(

    @Email(message= "연락 받을 이메일이 유효하지 않습니다.")
    @NotBlank(message = "연락 받을 이메일은 공백일 수 없습니다.")
    @Schema(description = "연락 받을 이메일")
    val contactEmail: String,

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Schema(description = "이름")
    val name: String,

    @NotBlank(message = "학교명은 공백일 수 없습니다.")
    @Schema(description = "학교명")
    val univName: String,

    @NotBlank(message = "전공명은 공백일 수 없습니다.")
    @Schema(description = "전공명")
    val major: String,

    @Schema(description = "연구실 정보")
    val labInfo: String?,

    @Schema(description = "광고성 정보 이메일/SMS 수신 동의 여부")
    var adConsent: Boolean,
)
