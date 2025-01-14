package com.dobby.backend.presentation.api.dto.request.experiment

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

data class Pagination (
    @field:Min(1)
    @Schema(description = "페이지네이션 - 기본값: 1페이지")
    val page: Int = 1,

    @field:Min(1)
    @Schema(description = "한 페이지 당 공고 수 - 기본값: 6개")
    val count: Int = 6,
)
