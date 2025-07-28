package com.dobby.api.dto.response.experiment

import com.dobby.model.experiment.keyword.ExperimentPostKeywords
import io.swagger.v3.oas.annotations.media.Schema

data class ExtractKeywordResponse(
    @Schema(description = "추출된 키워드 정보")
    val experimentPostKeywords: ExperimentPostKeywords
)
