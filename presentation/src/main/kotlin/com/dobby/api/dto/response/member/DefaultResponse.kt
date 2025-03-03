package com.dobby.api.dto.response.member

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "기본 Operation 응답")
data class DefaultResponse(
    @Schema(description = "성공 유무", example = "true")
    val success: Boolean
) {
    companion object {
        fun ok(): DefaultResponse = DefaultResponse(true)
        fun fail(): DefaultResponse = DefaultResponse(false)
    }
}
