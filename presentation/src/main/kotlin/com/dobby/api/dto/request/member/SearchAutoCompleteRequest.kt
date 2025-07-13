package com.dobby.api.dto.request.member

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class SearchAutoCompleteRequest(
    @NotBlank(message = "질의 텍스트는 공백일 수 없습니다.")
    @Schema(description = "자동완성 질의 텍스트")
    val query: String
)
