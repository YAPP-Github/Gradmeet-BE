package com.dobby.api.dto.response.member

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "학교 후보군 리스트 반환 DTO")
data class AutoCompleteResponse (
    @Schema(description = "학교 후보군 리스트")
    val result: List<String>
)
