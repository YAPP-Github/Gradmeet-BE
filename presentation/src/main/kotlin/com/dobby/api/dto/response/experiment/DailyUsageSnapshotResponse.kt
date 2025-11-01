package com.dobby.api.dto.response.experiment

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "AI 공고 등록 일일 사용 횟수 관련 응답")
data class DailyUsageSnapshotResponse(

    @Schema(description = "현재 사용한 횟수", example = "1")
    val count: Long,

    @Schema(description = "현재 사용한 횟수", example = "3")
    val limit: Int,

    @Schema(description = "현재 남은 사용 횟수", example = "2")
    val remainingCount: Long,

    @Schema(description = "초기화 시각", example = "2025-11-01T05:59:28.565Z")
    val resetsAt: LocalDateTime
)
