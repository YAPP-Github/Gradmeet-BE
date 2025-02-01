package com.dobby.backend.presentation.api.dto.response.member

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class MyExperimentPostResponse(
    @Schema(description = "실험 공고 ID", example = "01HGXN4H4PXNRH4")
    val experimentPostId: String,

    @Schema(description = "실험 공고 제목", example = "테스트 실험 공고")
    val title: String,

    @Schema(description = "실험 공고 요약", example = "실험 공고의 요약(주요) 정보")
    val content: String,

    @Schema(description = "조회수", example = "123")
    val views: Int,

    @Schema(description = "모집 상태 여부", example = "false")
    val recruitStatus: Boolean,

    @Schema(description = "업로드 날짜", example = "2025-01-01")
    val uploadDate: LocalDate
)
