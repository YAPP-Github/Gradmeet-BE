package com.dobby.api.dto.response.experiment

import io.swagger.v3.oas.annotations.media.Schema

data class ExperimentPostDetailRelatedResponse(
    @Schema(description = "연관된 '둘러보기' 공고 리스트")
    val relatedPosts: List<PostInfo>
)
