package com.dobby.backend.presentation.api.dto.response.member

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "공고 등록 시, 자동 입력에 필요한 연구자 정보 반환 DTO")
data class ResearcherInfoResponse(
    @Schema(description = "연구 책임")
    val leadResearcher: String,

    @Schema(description = "대학교 이름")
    val univName: String,
)
