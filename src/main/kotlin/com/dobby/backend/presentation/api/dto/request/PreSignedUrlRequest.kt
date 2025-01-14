package com.dobby.backend.presentation.api.dto.request

import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "PreSingedUrl 요청")
@JvmRecord
data class PreSignedUrlRequest(
    @Schema(description = "파일 이름(확장자 포함)", example = "image.jpg")
    val fileName: String,
)
