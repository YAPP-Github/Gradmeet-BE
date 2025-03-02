package com.dobby.api.dto.response.member

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "연구자 정보 조회 DTO")
data class ResearcherInfoResponse(
    @Schema(description = "사용자 기본 정보")
    val memberInfo: MemberResponse,

    @Schema(description = "대학교 메일")
    val univEmail: String,

    @Schema(description = "대학교 이름")
    val univName: String,

    @Schema(description = "대학교 전공")
    val major: String,

    @Schema(description = "연구실 정보")
    val labInfo: String?,

    @Schema(description = "광고성 정보 이메일/SMS 수신 동의 여부")
    val adConsent: Boolean,
)
