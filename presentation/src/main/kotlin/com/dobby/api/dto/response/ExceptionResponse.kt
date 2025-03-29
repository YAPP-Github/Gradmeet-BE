package com.dobby.api.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "에러(오류) 응답")
data class ExceptionResponse(
    @Schema(description = "에러 코드", example = "DE0001")
    val code: String,

    @Schema(description = "에러 메세지", example = "인가되지 않은 접근입니다.")
    val message: String,

    @Schema(description = "관련 데이터")
    val data: Any? = null
)
