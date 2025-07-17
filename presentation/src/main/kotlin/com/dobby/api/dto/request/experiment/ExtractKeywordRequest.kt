package com.dobby.api.dto.request.experiment

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class ExtractKeywordRequest(
    @NotBlank
    @Schema(description = "키워드를 추출할 텍스트", example = "심리학 실험 참여자를 모집합니다. 인지 심리학 관련 실험으로 약 30분 소요됩니다.")
    val text: String
)
